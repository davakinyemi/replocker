import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {catchError, finalize, Observable, of, tap} from 'rxjs';
import {ReportResponse} from '../../../../services/openapi/models/report-response';
import {ReportCollectionResponse} from '../../../../services/openapi/models/report-collection-response';
import {ActivatedRoute} from '@angular/router';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {TitleService} from '../../services/title/title.service';
import {map} from 'rxjs/operators';
import {AuthService} from '../../services/auth/auth.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-report-list',
  standalone: false,
  templateUrl: './report-list.component.html',
  styleUrl: './report-list.component.scss'
})
export class ReportListComponent implements OnInit, AfterViewInit {
  reports$!: Observable<ReportResponse[]>;
  collection$!: Observable<ReportCollectionResponse>;
  displayedColumns = ['name', 'type', 'download'];
  dataSource = new MatTableDataSource<ReportResponse>([]);
  isLoading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private route: ActivatedRoute,
    private reportService: ReportCollectionControllerService,
    private authService: AuthService,
    private titleService: TitleService,
    private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    // this.titleService.setTitle(this.route.snapshot.data['title']);

    const collectionId = this.route.snapshot.paramMap.get('collectionId')!;

    this.collection$ = this.reportService.getPublishedCollection({ collectionId }).pipe(
      tap(collection => this.titleService.setTitle(collection.name!))
    );

    this.reports$ = this.reportService.getReports({ collectionId }).pipe(
      map(response => response.content || []),
      catchError(() => of([])),
      finalize(() => this.isLoading = false)
    );
  }

  ngAfterViewInit() {
    this.reports$.subscribe(data => {
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  downloadReport(report: ReportResponse) {
    const reportId = report.id!
    const collectionId = this.route.snapshot.paramMap.get('collectionId')!;
    const token = this.authService.getValidToken(collectionId);
    this.reportService.downloadReport({
      collectionId: this.route.snapshot.paramMap.get('collectionId')!,
      reportId,
      accessToken: token!,
    }).subscribe(() => {});
  }
}

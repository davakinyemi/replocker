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
import {TokenAuthService} from '../../services/token-auth/token-auth.service';
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
  displayedColumns = ['name', 'createdDate', 'type', 'download'];
  dataSource = new MatTableDataSource<ReportResponse>([]);
  isLoading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private route: ActivatedRoute,
    private reportService: ReportCollectionControllerService,
    private authService: TokenAuthService,
    private titleService: TitleService,
    private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    // this.titleService.setTitle(this.route.snapshot.data['title']);

    const collectionId = this.route.snapshot.paramMap.get('collectionId')!;
    const accessToken = this.authService.getValidToken(collectionId)!;

    /* this.collection$ = this.reportService.getPublishedCollection({ collectionId, accessToken }).pipe(
      tap(collection => this.titleService.setTitle(collection.name!))
    ); */

    this.reportService.getPublishedCollection({ collectionId, accessToken }).pipe(
      tap(collection => this.titleService.setTitle(collection.name!)),
      finalize(() => this.isLoading = false)
    ).subscribe();

    this.reports$ = this.reportService.getReports({ collectionId, accessToken }).pipe(
      map(response => response.content || []),
      catchError(() => of([])),
      finalize(() => this.isLoading = false)
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.reports$.subscribe(data => this.dataSource.data = data);
  }

  downloadReport(report: ReportResponse) {
    const reportId = report.id!
    const collectionId = this.route.snapshot.paramMap.get('collectionId')!;
    const token = this.authService.getValidToken(collectionId);
    this.reportService.downloadReport({
      collectionId: this.route.snapshot.paramMap.get('collectionId')!,
      reportId,
      accessToken: token!,
    }).subscribe({
      next: (blob) => this.handleFileDownload(blob, reportId),
      error: () => this.snackBar.open(
        'Failed to download report',
        'Retry',
        { duration: 5000 }
      ).onAction().subscribe(() => this.downloadReport(report))
    });
  }

  private handleFileDownload(blob: Blob, reportId: string) {
    if (blob.size === 0) {
      this.snackBar.open('Empty file content', 'Close', { duration: 3000 });
      return;
    }

    const report = this.dataSource.data.find(r => r.id === reportId);
    const fileName = this.getFileName(report);

    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');

    link.href = url;
    link.download = fileName;
    link.style.display = 'none';

    document.body.appendChild(link);
    link.click();

    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
  }

  private getFileName(report?: ReportResponse): string {
    const baseName = report?.name?.replace(/[^a-z0-9]/gi, '_') || 'report';
    const extension = report?.type?.toLowerCase() || 'bin';
    return `${baseName}.${extension}`;
  }
}

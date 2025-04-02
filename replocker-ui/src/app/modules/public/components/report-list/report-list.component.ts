import {Component, OnInit} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {ReportResponse} from '../../../../services/openapi/models/report-response';
import {ReportCollectionResponse} from '../../../../services/openapi/models/report-collection-response';
import {ActivatedRoute} from '@angular/router';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {TitleService} from '../../services/title/title.service';
import {map} from 'rxjs/operators';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-report-list',
  standalone: false,
  templateUrl: './report-list.component.html',
  styleUrl: './report-list.component.scss'
})
export class ReportListComponent implements OnInit {
  reports$!: Observable<ReportResponse[]>;
  collection$!: Observable<ReportCollectionResponse>;

  constructor(
    private route: ActivatedRoute,
    private reportService: ReportCollectionControllerService,
    private authService: AuthService,
    private titleService: TitleService
  ) {
  }

  ngOnInit() {
    const collectionId = this.route.snapshot.paramMap.get('collectionId')!;

    this.collection$ = this.reportService.getPublishedCollection({ collectionId }).pipe(
      tap(collection => this.titleService.setTitle(collection.name!))
    );

    this.reports$ = this.reportService.getReports({ collectionId }).pipe(
      map(response => response.content || [])
    );
  }

  downloadReport(reportId: string) {
    const token = this.authService.getValidToken(this.route.snapshot.paramMap.get('collectionId')!);
    this.reportService.downloadReport({
      collectionId: this.route.snapshot.paramMap.get('collectionId')!,
      reportId,
      accessToken: token!,
    }).subscribe(blob => {

    });
  }
}

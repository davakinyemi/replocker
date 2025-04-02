import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {catchError, finalize, Observable, of} from 'rxjs';
import {ReportCollectionResponse} from '../../../../services/openapi/models/report-collection-response';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {MatDialog} from '@angular/material/dialog';
import {TitleService} from '../../services/title/title.service';
import {map} from 'rxjs/operators';
import {Router} from '@angular/router';
import {
  ReportCollectionLockedDialogComponent
} from '../report-collection-locked-dialog/report-collection-locked-dialog.component';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-report-collection-list',
  standalone: false,
  templateUrl: './report-collection-list.component.html',
  styleUrl: './report-collection-list.component.scss'
})
export class ReportCollectionListComponent implements OnInit, AfterViewInit {
  collections$: Observable<ReportCollectionResponse[]> = of([]);
  dataSource = new MatTableDataSource<ReportCollectionResponse>([]);
  displayedColumns = ['name', 'description', 'reports', 'status'];
  isLoading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private router: Router,
    private reportCollectionService: ReportCollectionControllerService,
    private titleService: TitleService,
    private authService: AuthService,
    public dialog: MatDialog,
  ) {
  }

  ngOnInit() {
    this.titleService.setTitle('Report Collections');
    this.loadCollections();
  }

  ngAfterViewInit() {
    this.collections$.subscribe(data => {
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  private loadCollections() {
    this.collections$ = this.reportCollectionService.getPublishedCollections().pipe(
      map(response => response.content || []),
      // startWith([]),
      catchError(() => of([])),
      finalize(() => this.isLoading = false)
    );
  }

  viewCollection(collection: ReportCollectionResponse) {
    if (collection.locked) {
      const token = this.getAccessToken(collection.id!);
      if (token) {
        this.router.navigate(['/collections', collection.id]).then();
      } else {
        this.dialog.open(ReportCollectionLockedDialogComponent, {
          data: { collectionId: collection.id }
        }).afterClosed().subscribe(result => {
          if (result?.accessGranted) {
            this.router.navigate(['/collections', collection.id]).then();
          }
        });
      }
    } else {
      this.router.navigate(['/collections', collection.id]).catch(error => console.error('Navigation failed:', error));
    }
  }

  private getAccessToken(collectionId: string) {
    return this.authService.getValidToken(collectionId)!;
  }
}

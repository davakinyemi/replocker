import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import {PublicRoutingModule} from './public-routing.module';
import {ReportCollectionListComponent} from './components/report-collection-list/report-collection-list.component';
import {ReportListComponent} from './components/report-list/report-list.component';
import {
  ReportCollectionLockedDialogComponent
} from './components/report-collection-locked-dialog/report-collection-locked-dialog.component';
import {MatDialogContent} from "@angular/material/dialog";
import {ReactiveFormsModule} from '@angular/forms';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton, MatIconButton} from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatIcon} from '@angular/material/icon';
import {MatChip} from '@angular/material/chips';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatProgressBar} from '@angular/material/progress-bar';
import {HeaderComponent} from './components/header/header.component';
import {MatToolbar} from '@angular/material/toolbar';
import { FooterComponent } from './components/footer/footer.component';
import { LayoutComponent } from './components/layout/layout.component';


@NgModule({
  declarations: [
    ReportCollectionListComponent,
    ReportListComponent,
    ReportCollectionLockedDialogComponent,
    HeaderComponent,
    FooterComponent,
    LayoutComponent,
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    MatDialogContent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatButton,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    MatIcon,
    MatChip,
    MatRow,
    MatRowDef,
    MatIconButton,
    MatSort,
    MatSortHeader,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatProgressBar,
    MatToolbar,
    NgOptimizedImage
  ]
})
export class PublicModule { }

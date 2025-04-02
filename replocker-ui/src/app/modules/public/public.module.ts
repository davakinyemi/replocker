import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PublicRoutingModule } from './public-routing.module';
import { ReportCollectionListComponent } from './components/report-collection-list/report-collection-list.component';
import { ReportListComponent } from './components/report-list/report-list.component';
import { ReportCollectionLockedDialogComponent } from './components/report-collection-locked-dialog/report-collection-locked-dialog.component';
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
  MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatIcon} from '@angular/material/icon';
import {MatChip} from '@angular/material/chips';


@NgModule({
  declarations: [
    ReportCollectionListComponent,
    ReportListComponent,
    ReportCollectionLockedDialogComponent
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
    MatIconButton
  ]
})
export class PublicModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatToolbar, MatToolbarModule} from '@angular/material/toolbar';
import {MatButton, MatButtonModule, MatIconButton} from '@angular/material/button';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatChip} from '@angular/material/chips';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatDialogContent} from '@angular/material/dialog';

const materialModules = [
  MatIconModule,
  MatDialogContent,
  MatToolbarModule,
  MatButtonModule,
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
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    materialModules
  ],
  exports: [
    materialModules
  ]
})
export class MaterialModule { }

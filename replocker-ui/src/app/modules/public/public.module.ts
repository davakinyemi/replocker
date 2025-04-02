import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import {PublicRoutingModule} from './public-routing.module';
import {ReportCollectionListComponent} from './components/report-collection-list/report-collection-list.component';
import {ReportListComponent} from './components/report-list/report-list.component';
import {
  ReportCollectionLockedDialogComponent
} from './components/report-collection-locked-dialog/report-collection-locked-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {LayoutComponent} from './components/layout/layout.component';
import {MaterialModule} from '../../material/material.module';


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
    ReactiveFormsModule,
    MaterialModule,
    NgOptimizedImage
  ]
})
export class PublicModule { }

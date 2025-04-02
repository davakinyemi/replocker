import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ReportCollectionListComponent} from './components/report-collection-list/report-collection-list.component';
import {ReportListComponent} from './components/report-list/report-list.component';
import {reportCollectionResolver} from './components/report-list/report-collection.resolver';

const routes: Routes = [
  {
    path: '',
    component: ReportCollectionListComponent,
    data: { title: 'Report Collections' }
  },
  {
    path: 'collections/:collectionId',
    component: ReportListComponent,
    resolve: {
      collection: reportCollectionResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicRoutingModule { }

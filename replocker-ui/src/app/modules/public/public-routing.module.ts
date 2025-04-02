import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ReportCollectionListComponent} from './components/report-collection-list/report-collection-list.component';
import {ReportListComponent} from './components/report-list/report-list.component';
import {reportCollectionResolver} from './components/report-list/report-collection.resolver';
import {LayoutComponent} from './components/layout/layout.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: ReportCollectionListComponent,
        data: { title: 'Report Collections' }
      },
      {
        path: ':collectionId',
        component: ReportListComponent,
        resolve: {
          collection: reportCollectionResolver
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Role } from './core/models/role.enum';
import {authGuard} from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'collections',
    pathMatch: 'full'
  },
  {
    path: 'collections',
    loadChildren: () => import('./modules/public/public.module').then(m => m.PublicModule),
    data: { title: 'Report Collections' }
  },
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule),
    canActivate: [authGuard],
    data: {
      roles: [Role.REPLOCKER_ADMIN],
      title: 'Admin Dashboard',
    }
  },
  {
    path: '**',
    redirectTo: 'collections',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

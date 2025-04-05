import {ResolveFn, Router} from '@angular/router';
import {ReportCollectionResponse} from '../../../../services/openapi/models/report-collection-response';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {inject} from '@angular/core';
import {TokenAuthService} from '../../services/token-auth/token-auth.service';
import {catchError, throwError} from 'rxjs';

export const reportCollectionResolver: ResolveFn<ReportCollectionResponse> = (route) => {
  const reportService = inject(ReportCollectionControllerService);
  const auth = inject(TokenAuthService);
  const router = inject(Router);
  const collectionId = route.paramMap.get('collectionId')!;
  const token = auth.getValidToken(collectionId);

  /* return reportService.getPublishedCollection({ collectionId }).pipe(
    catchError((error) => {
      if (error.status === 403) {
        // Collection is locked, require token
        const token = auth.getValidToken(collectionId);
        if (!token) {
          router.navigate(['/collections'], {
            queryParams: { error: 'LOCKED_NO_TOKEN' }
          }).catch(() => {});
          return throwError(() => new Error('Locked collection requires token'));
        }
        return reportService.getPublishedCollection({
          collectionId,
          accessToken: token
        });
      }
      return throwError(() => error);
    })
  ); */

  return inject(ReportCollectionControllerService)
    .getPublishedCollection({
      collectionId,
      accessToken: token || undefined
    }).pipe(
      catchError(error => {
        if (error.status === 403) {
          auth.storeToken(collectionId, ''); // Clear invalid token
          inject(Router).navigate(['/collections']).then();
        }
        return throwError(() => error);
      })
    );
};

import {ResolveFn, Router} from '@angular/router';
import {ReportCollectionResponse} from '../../../../services/openapi/models/report-collection-response';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {inject} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {catchError, throwError} from 'rxjs';

export const reportCollectionResolver: ResolveFn<ReportCollectionResponse> = (route) => {
  const reportService = inject(ReportCollectionControllerService);
  const auth = inject(AuthService);
  const router = inject(Router);
  const collectionId = route.paramMap.get('collectionId')!;

  return reportService.getPublishedCollection({ collectionId }).pipe(
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
  );

  // const token = auth.getValidToken(collectionId)!;

  /* if (!token) {
    router.navigate(['/collections'], {
      queryParams: { error: 'NO_TOKEN' }
    }).catch(() => {});
    return throwError(() => new Error('No access token'));
  }

  return reportService.getPublishedCollection({ collectionId, accessToken: token }).pipe(
    catchError((error) => {
      if (error.status === 403) {
        router.navigate(['/collections'], {
          queryParams: { error: 'INVALID_TOKEN' }
        }).catch(() => {});
      }
      return throwError(() => error);
    })
  ); */
};

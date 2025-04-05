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
  const tokenAuth = inject(TokenAuthService);
  const router = inject(Router);
  const collectionId = route.paramMap.get('collectionId')!;
  const token = tokenAuth.getValidToken(collectionId);

  return reportService
    .getPublishedCollection({
      collectionId,
      accessToken: token || undefined
    }).pipe(
      catchError(error => {
        if (error.status === 403) {
          tokenAuth.storeToken(collectionId, ''); // Clear invalid token
          router.navigate(['/collections']).then();
        }
        return throwError(() => error);
      })
    );
};

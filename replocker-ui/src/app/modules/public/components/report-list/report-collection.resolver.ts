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
  const token = auth.getValidToken(collectionId)!;

  return reportService.getPublishedCollection({ collectionId, accessToken: token }).pipe(
    catchError((error) => {
      if (error.status === 403) {
        router.navigate(['/collections'], {
          queryParams: { error: 'INVALID_TOKEN' }
        }).catch(() => {});
      }
      return throwError(() => error);
    })
  );
};

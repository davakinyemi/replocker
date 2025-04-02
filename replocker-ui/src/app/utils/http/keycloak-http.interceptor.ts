import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloak.service';
import {catchError, defer, mergeMap, of, retry, retryWhen, switchMap, take, throwError, timer} from 'rxjs';

export const keycloakHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const kcService = inject(KeycloakService);
  const publicRoutes = [
    '/api/report-collections/public',
    '/realms/replocker-app/protocol/openid-connect/token'
  ];

  if (publicRoutes.some(route => req.url.includes(route))) {
    return next(req);
  }

  return defer(() => {
    if (kcService.isTokenValid()) {
      return [kcService.token$];
    }
    return kcService.refreshToken();
  }).pipe(
    take(1),
    switchMap(token => {
      if (!token) {
        return throwError(() => new Error('No valid token available'));
      }

      const authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` },
      });

      return next(authReq).pipe(
        catchError(error => {
          if (error.status === 401) {
            return handleUnauthorized(kcService, req, next);
          }
          return throwError(() => error);
        })
      );
    }),
    retry({
      count: 1,
      delay: (error, retryCount) => {
        if (error.status === 401 && retryCount === 1) {
          return kcService.login().pipe(
            switchMap(() => timer(100))
          );
        }
        return throwError(() => error);
      }
    })
  );
};

function handleUnauthorized(kcService: KeycloakService, req: Parameters<HttpInterceptorFn>[0], next: Parameters<HttpInterceptorFn>[1]) {
  return kcService.logout().pipe(
    switchMap(() => next(req)),
    catchError(() => next(req))
  );
}

function handleUnauthorizedRetry(kcService: KeycloakService) {
  return kcService.login().pipe(
    switchMap(() => of(void 0)),
  );
}

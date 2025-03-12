import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloak.service';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(KeycloakService).keycloak.token;

  if (token) {
    const authRequest = req.clone({
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
      /*headers: req.headers.append('Authorization', 'Bearer ' + token)*/
    });
    return next(authRequest);
  }
  return next(req);
};

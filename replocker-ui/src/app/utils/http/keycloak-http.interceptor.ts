import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloak.service';

export const keycloakHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const kcService = inject(KeycloakService);
  const token = kcService.keycloak.token;
  if (token) {
    const authRequest = req.clone({
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
    return next(authRequest);
  }
  return next(req);
};

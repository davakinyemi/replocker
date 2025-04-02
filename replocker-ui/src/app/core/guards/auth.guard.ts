import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {Role} from '../models/role.enum';

export const authGuard: CanActivateFn = (route, state) => {
  const kcService = inject(KeycloakService);
  const router = inject(Router);

  const requiredRoles = route.data['roles'] as Role[];

  if (!kcService.isAuthenticated()) {
    return router.createUrlTree(['/']);
  }

  if (requiredRoles && !kcService.hasAnyRole(requiredRoles)) {
    return router.createUrlTree(['/forbidden']);
  }

  return true;
};

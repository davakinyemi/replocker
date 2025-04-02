import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {AuthService} from '../../modules/public/services/auth/auth.service';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError, throwError} from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  const showValidationErrors = (errors: { [key: string]: string[] }) => {
    const messages = Object.values(errors).flat().join('\n');
    snackBar.open(messages, 'Close', { duration: 7000 });
  };

  return next(req).pipe(
    catchError((error) => {
      if (error instanceof HttpErrorResponse) {
        switch (error.status) {
          case 403:
            snackBar.open('Invalid access token', 'Close', { duration: 5000 })
            break;
          case 404:
            router.navigate(['/not-found']).catch(error => {});
            break;
          case 422:
            showValidationErrors(error.error.errors)
            break;
          default:
            snackBar.open('Server error. Try again later', 'Close');
        }
      }
      return throwError(() => error);
    })
  );
};

import {Injectable, OnDestroy} from '@angular/core';
import Keycloak from 'keycloak-js';
import {environment} from '../../../environments/environment';
import {
  BehaviorSubject,
  catchError,
  distinctUntilChanged,
  from,
  of,
  ReplaySubject,
  shareReplay,
  switchMap,
  tap,
  throwError
} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService implements OnDestroy {
  private readonly _keycloak!: Keycloak;
  private _token$ = new ReplaySubject<string>(1);
  private _authState$ = new BehaviorSubject<boolean>(false);
  private _refreshInProgress$ = new BehaviorSubject<boolean>(false);
  private _redirectUri = environment.production
    ? 'https://your-production-url.com'
    : 'http://localhost:4200';

  constructor() {
    this._keycloak = new Keycloak({
      url: environment.keycloak.url,
      realm: environment.keycloak.realm,
      clientId: environment.keycloak.clientId
    });

    this._keycloak.onAuthRefreshError = () =>
      this.handleAuthError('Token refresh failed');
  }

  ngOnDestroy() {
    this._token$.complete();
    this._authState$.complete();
    this._refreshInProgress$.complete();
  }

  get keycloak() {
    return this._keycloak!;
  }

  init() {
    return from(this._keycloak.init(
      {
        onLoad: 'check-sso',
        checkLoginIframe: false,
        pkceMethod: 'S256',
        redirectUri: this._redirectUri
      }
    )).pipe(
      tap((authenticated: boolean) => {
        this._authState$.next(authenticated);
        if (authenticated) this.updateTokenStream();
      }),
      catchError(error => {
        this.handleAuthError('Initialization failed', error);
        return of(false);
      })
    );
  }

  get token$() {
    return this._token$.asObservable().pipe(
      distinctUntilChanged(),
    );
  }

  get authState$() {
    return this._authState$.asObservable();
  }

  get refreshInProgress$() {
    return this._refreshInProgress$.asObservable();
  }

  refreshToken() {
    if (this._refreshInProgress$.value) {
      return this._token$;
    }

    this._refreshInProgress$.next(true);

    return from(this._keycloak.updateToken(30)).pipe(
      tap(() => {
        this.updateTokenStream();
        this._refreshInProgress$.next(false);
      }),
      switchMap((refreshed) => {
        if (!refreshed || !this._keycloak.token) {
          return throwError(() => new Error('Token refresh failed'));
        }
        return of(this._keycloak.token);
      }),
      catchError(error => {
        this._refreshInProgress$.next(false);
        this.handleAuthError('Refresh failed', error);
        return throwError(() => error);
      }),
      shareReplay(1)
    );
  }

  login() {
    return from(this._keycloak.login({
      redirectUri: this._redirectUri,
      scope: 'openid profile email',
    })).pipe(
      tap(() => {
        this._authState$.next(true);
        this.updateTokenStream();
      }),
      catchError(error => {
        this.handleAuthError('Login failed', error);
        return throwError(() => error);
      })
    );
  }

  logout() {
    return from(this._keycloak.logout({ redirectUri: this._redirectUri })).pipe(
      tap(() => {
        this._authState$.next(false);
        this._token$.next('');
      }),
      catchError(error => {
        this.handleAuthError('Logout failed', error);
        return throwError(() => error);
      })
    );
  }

  private updateTokenStream() {
    if (this._keycloak.token) {
      this._token$.next(this._keycloak.token);
    }
  }

  private handleAuthError(context: string, error?: unknown): void {
    console.error(`Keycloak Error: ${context}`, error);
    this._authState$.next(false);
    this._token$.next('');
  }

  isTokenValid(thresholdSeconds = 30): boolean {
    return !!this._keycloak.token && !this._keycloak.isTokenExpired(thresholdSeconds);
  }

  public isAuthenticated(): boolean {
    return this._keycloak.authenticated || false;
  }

  getTokenExpiration(): number {
    return this._keycloak.tokenParsed?.exp ?? 0;
  }

  hasAnyRole(requiredRoles: string[]): boolean {
    if (!requiredRoles || requiredRoles.length === 0) return true;

    const token = this._keycloak.tokenParsed;
    if (!token) return false;

    const clientRoles = token.resource_access?.[environment.keycloak.clientId]?.roles || [];
    const realmRoles = token.realm_access?.roles || [];

    return [...clientRoles, ...realmRoles].some(role =>
      requiredRoles.includes(role)
    );
  }

  accountManagement() {
    return this._keycloak.accountManagement();
  }
}

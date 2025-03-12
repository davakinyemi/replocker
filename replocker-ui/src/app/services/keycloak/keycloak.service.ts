import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js/lib/keycloak';
import {AuthProfile} from './auth-profile';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private readonly _keycloak: Keycloak;
  private _profile: AuthProfile | undefined;
  private _token: string | undefined;
  private _redirectUri: string = 'http://localhost:4200';

  constructor() {
    this._keycloak = new Keycloak({
      url: 'http://localhost:9090',
      realm: 'replocker',
      clientId: 'replocker'
    });
  }

  get keycloak() {
    return this._keycloak;
  }

  set redirectUri(uri: string) {
    this._redirectUri = uri;
  }

  get redirectUri() {
    return this._redirectUri;
  }

  get profile$(): Observable<AuthProfile> {
    return new Observable<AuthProfile>(subscriber => {
      if (this._profile) {
        subscriber.next(this._profile);
      } else {
        this._keycloak.loadUserProfile().then(profile => {
          if (this._profile) {
            subscriber.next(this._profile);
          }
        });
      }
    });
  }

  async init() {
    console.log("Authenticating...");

    try {
      const authenticated = await this._keycloak.init({
        onLoad: 'login-required',
        checkLoginIframe: false
      });

      if (authenticated) {
        console.log("Authenticated...");
        this._profile = (await this._keycloak.loadUserProfile()) as AuthProfile;
        this._token = this._keycloak.token;
      }
      return authenticated;
    } catch (error) {
      console.error("Keycloak initialization failed: ", error);
      return false;
    }
  }

  async refreshToken() {
    try {
      const refreshed = await this._keycloak.updateToken(30);
      if (refreshed) {
        this._token = this._keycloak.token;
      }
      return refreshed;
    } catch (error) {
      console.log("Authentication token refresh failed: ", error);
      return false;
    }
  }

  login() {
    return this._keycloak.login();
  }

  logout() {
    return this._keycloak.logout({
      redirectUri: this._redirectUri
    });
  }

  manageAccountFromKeycloak() {
    return this._keycloak.accountManagement();
  }
}

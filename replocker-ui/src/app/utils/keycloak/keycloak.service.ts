import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;
  private _redirectUri: string = 'http://localhost:4200';

  constructor() {}

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'replocker',
        clientId: 'replocker-app'
      });
    }
    return this._keycloak;
  }

  async init() {
    console.log("Authenticating...");
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false
    });
  }

  async login() {
    await this.keycloak.login();
  }

  get adminId() {
    return this.keycloak?.tokenParsed?.sub as string;
  }

  get isTokenValid() {
    return !this.keycloak?.isTokenExpired();
  }

  get username(): string {
    return this.keycloak.tokenParsed?.['username'] as string;
  }

  logout() {
    return this.keycloak.logout({
      redirectUri: this._redirectUri
    });
  }

  accountManagement() {
    return this.keycloak.accountManagement();
  }

}

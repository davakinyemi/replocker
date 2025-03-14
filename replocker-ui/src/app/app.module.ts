import {inject, NgModule, provideAppInitializer} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {MaterialModule} from './material/material.module';
import {KeycloakService} from './utils/keycloak/keycloak.service';
import {keycloakHttpInterceptor} from './utils/http/keycloak-http.interceptor';

export function initializeKeycloak() {
  const kcService = inject(KeycloakService);
  return kcService.init();
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule
  ],
  providers: [
    provideHttpClient(
      withInterceptors([keycloakHttpInterceptor])
    ),
    provideAppInitializer(initializeKeycloak)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

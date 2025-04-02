import {inject, NgModule, provideAppInitializer} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {KeycloakService} from './utils/keycloak/keycloak.service';
import {keycloakHttpInterceptor} from './utils/http/keycloak-http.interceptor';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

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
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideHttpClient(
      withInterceptors([
        keycloakHttpInterceptor,
        // errorInterceptor
      ])
    ),
    provideAppInitializer(initializeKeycloak)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

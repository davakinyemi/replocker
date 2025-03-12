import {inject, NgModule, provideAppInitializer} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {httpTokenInterceptor} from './services/interceptor/http-token.interceptor';
import {KeycloakService} from './services/keycloak/keycloak.service';

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
    AppRoutingModule
  ],
  providers: [
    provideHttpClient(
      withInterceptors([httpTokenInterceptor])
    ),
    provideAppInitializer(initializeKeycloak)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

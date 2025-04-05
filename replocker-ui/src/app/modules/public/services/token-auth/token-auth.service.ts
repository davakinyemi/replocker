import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {filter} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TokenAuthService {

  private readonly TOKEN_PREFIX = 'rep_token_';

  private tokenValidity = new Subject<boolean>();

  constructor() {
  }

  storeToken(collectionId: string, token: string) {
    const expiry = Date.now() + 30 * 60 * 1000;
    sessionStorage.setItem(
      `${this.TOKEN_PREFIX}${collectionId}`,
      JSON.stringify({ token , expiry }),
    );
  }

  getValidToken(collectionId: string): string | null {
    const item = sessionStorage.getItem(`${this.TOKEN_PREFIX}${collectionId}`);
    if (!item) return null;

    const { token, expiry } = JSON.parse(item);
    return Date.now() < expiry ? token : null;
  }

  watchTokenValidity(collectionId: string): Observable<boolean> {
    return this.tokenValidity.pipe(
      filter(() => !!this.getValidToken(collectionId))
    );
  }
}

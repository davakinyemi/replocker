import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_PREFIX = 'rep_token_';

  constructor(private http: HttpClient) {
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
}

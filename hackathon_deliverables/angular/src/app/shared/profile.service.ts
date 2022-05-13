import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Profile } from './profile';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  // Base url
  //baseurl = 'http://localhost:3000';
  baseUrl = 'https://springboot-wespeakright-speaknow.azuremicroservices.io/api/textToSpeech/';
  constructor(private http: HttpClient) {}
  // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };
  // POST
  CreateProfile(data): Observable<Profile> {
    return this.http
      .post<Profile>(
        this.baseUrl + 'record',
        JSON.stringify(data),
        this.httpOptions
      )
      .pipe(retry(1), catchError(this.errorHandl));
  }
  // GET
  GetProfile(id): Observable<Profile> {
    return this.http
      .get<Profile>(this.baseUrl + id)
      .pipe(retry(0), catchError(this.errorHandl));
  }

  // GET
  GetProfiles(): Observable<Profile> {
    return this.http
      .get<Profile>(this.baseUrl + 'list/allPersons')
      .pipe(retry(1), catchError(this.errorHandl));
  }

  // DELETE
  DeleteProfile(id) {
    return this.http
      .delete<Profile>(this.baseUrl + id , this.httpOptions)
      .pipe(retry(1), catchError(this.errorHandl));
  }
  // Error handling
  errorHandl(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(() => {
      return errorMessage;
    });
  }
}

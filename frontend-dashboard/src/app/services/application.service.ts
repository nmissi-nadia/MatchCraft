import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Application, Page } from '../models/application.model';
import { ApplicationStatus } from '../models/application-status.enum';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = 'http://localhost:8080/api/applications';

  constructor(private http: HttpClient) {}

  private handleError(error: any) {
    console.error('API Error:', error);
    return throwError(() => new Error(error.message || 'Server error'));
  }

  getApplications(statut?: ApplicationStatus, page: number = 0, size: number = 20): Observable<Page<Application>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (statut) {
      params = params.set('statut', statut);
    }
    
    return this.http.get<Page<Application>>(this.apiUrl, { params })
      .pipe(catchError(this.handleError));
  }

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  updateStatus(id: number, statut: ApplicationStatus): Observable<Application> {
    return this.http.patch<Application>(`${this.apiUrl}/${id}/status`, { statut })
      .pipe(catchError(this.handleError));
  }

  sendApplication(id: number, payload: { sujetMail: string, corpsMail: string }): Observable<Application> {
    return this.http.post<Application>(`${this.apiUrl}/${id}/send`, payload)
      .pipe(catchError(this.handleError));
  }

  updateDocuments(id: number, sujetMail: string, corpsMail: string): Observable<Application> {
    return this.http.put<Application>(`${this.apiUrl}/${id}/documents`, { sujetMail, corpsMail })
      .pipe(catchError(this.handleError));
  }

  exportApplicationsCsv(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export`, { responseType: 'blob' })
      .pipe(catchError(this.handleError));
  }

  runScraper(keyword: string): Observable<any> {
    const payload = {
      keyword: keyword,
      user_id: 1
    };
    return this.http.post('http://localhost:8000/agent/run-scraper', payload)
      .pipe(catchError(this.handleError));
  }
}

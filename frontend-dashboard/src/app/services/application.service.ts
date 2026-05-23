import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Application, Page } from '../models/application.model';
import { ApplicationStatus } from '../models/application-status.enum';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = '/api/applications'; // Sera proxyfié vers http://localhost:8080 en dev

  constructor(private http: HttpClient) {}

  getApplications(statut?: ApplicationStatus, page: number = 0, size: number = 20): Observable<Page<Application>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
      
    if (statut) {
      params = params.set('statut', statut);
    }
    
    return this.http.get<Page<Application>>(this.apiUrl, { params });
  }

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/${id}`);
  }

  updateStatus(id: number, statut: ApplicationStatus): Observable<Application> {
    return this.http.patch<Application>(`${this.apiUrl}/${id}/status`, { statut });
  }

  updateDocuments(id: number, sujetMail: string, corpsMail: string): Observable<Application> {
    return this.http.put<Application>(`${this.apiUrl}/${id}/documents`, { sujetMail, corpsMail });
  }
}

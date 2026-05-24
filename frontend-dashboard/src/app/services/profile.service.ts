import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserProfile {
    id?: number;
    prenom: string;
    nom: string;
    email: string;
    telephone: string;
    titreProfessionnel: string;
    competencesBase: string;
    githubUsername: string;
    cvTextuel: string;
    projects?: any[];
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = 'http://localhost:8080/api/profile';

  constructor(private http: HttpClient) { }

  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(this.apiUrl);
  }

  updateProfile(profile: UserProfile): Observable<UserProfile> {
    return this.http.put<UserProfile>(this.apiUrl, profile);
  }

  syncGithub(): Observable<any> {
    return this.http.post(`${this.apiUrl}/sync-github`, {});
  }
}

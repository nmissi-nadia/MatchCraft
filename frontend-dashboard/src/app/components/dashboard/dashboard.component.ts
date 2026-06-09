import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { Application } from '../../models/application.model';
import { ApplicationStatus } from '../../models/application-status.enum';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  applications: Application[] = [];
  statusEnum = ApplicationStatus;

  constructor(private applicationService: ApplicationService) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.applicationService.getApplications().subscribe({
      next: (page) => {
        this.applications = page.content;
      },
      error: (err) => console.error('Erreur lors du chargement des candidatures', err)
    });
  }

  getStatusClass(status: ApplicationStatus): string {
    switch(status) {
      case ApplicationStatus.PENDING_REVIEW: return 'bg-blue-100 text-blue-800 border-blue-200';
      case ApplicationStatus.APPROVED: return 'bg-green-100 text-green-800 border-green-200';
      case ApplicationStatus.REJECTED: return 'bg-red-100 text-red-800 border-red-200';
      case ApplicationStatus.SENT: return 'bg-indigo-100 text-indigo-800 border-indigo-200';
      case ApplicationStatus.INTERVIEW: return 'bg-purple-100 text-purple-800 border-purple-200';
      case ApplicationStatus.DECLINED: return 'bg-gray-100 text-gray-800 border-gray-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  }

  isScraping = false;
  
  triggerScraper(): void {
    this.isScraping = true;
    // On lance une recherche par défaut basée sur le profil (ex: Developpeur Fullstack Java Angular)
    this.applicationService.runScraper('Developpeur Fullstack').subscribe({
      next: (res: any) => {
        this.isScraping = false;
        // On recharge la liste une fois le scraping terminé
        this.loadApplications();
      },
      error: (err: any) => {
        console.error('Erreur lors du scraping', err);
        this.isScraping = false;
      }
    });
  }
}

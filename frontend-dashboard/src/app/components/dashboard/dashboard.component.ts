import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationService } from '../../services/application.service';
import { Application } from '../../models/application.model';
import { ApplicationStatus } from '../../models/application-status.enum';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
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
      case ApplicationStatus.PENDING_REVIEW: return 'bg-yellow-100 text-yellow-800';
      case ApplicationStatus.APPROVED: return 'bg-green-100 text-green-800';
      case ApplicationStatus.REJECTED: return 'bg-red-100 text-red-800';
      case ApplicationStatus.SENT: return 'bg-blue-100 text-blue-800';
      case ApplicationStatus.INTERVIEW: return 'bg-purple-100 text-purple-800';
      case ApplicationStatus.DECLINED: return 'bg-gray-100 text-gray-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }
}

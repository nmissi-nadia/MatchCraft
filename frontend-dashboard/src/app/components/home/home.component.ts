import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './home.component.html'
})
export class HomeComponent {
  isLoading = false;
  successMessage = '';
  
  constructor(private http: HttpClient, private router: Router) {}

  triggerMockScraping() {
    this.isLoading = true;
    this.successMessage = '';
    const payload = {
      titre: "Développeur Full-Stack Java/Angular",
      nom_entreprise: "Morocco Tech",
      localisation: "Casablanca",
      url: "https://www.linkedin.com/jobs/view/mock-demo-123",
      plateforme_source: "LinkedIn",
      description_brute: "Nous recherchons activement un profil Cloud et Web avec des compétences solides en architecture microservices et design d'UI.",
      estimated_relevance_score: 0.85
    };

    // Assuming we want to call the actual trigger endpoint for the agent
    this.http.post('http://localhost:8000/agent/scrape-trigger', payload).subscribe({
      next: (res) => {
        console.log('Scraping mocké avec succès', res);
        this.isLoading = false;
        this.successMessage = 'Ingestion simulée avec succès !';
        setTimeout(() => this.router.navigate(['/dashboard']), 1500);
      },
      error: (err) => {
        console.error('Erreur lors du scraping', err);
        this.isLoading = false;
        this.successMessage = 'Erreur lors de la simulation.';
        setTimeout(() => this.router.navigate(['/dashboard']), 1500);
      }
    });
  }
}

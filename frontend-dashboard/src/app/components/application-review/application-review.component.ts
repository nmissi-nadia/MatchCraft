import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { Application } from '../../models/application.model';
import { ApplicationStatus } from '../../models/application-status.enum';

@Component({
  selector: 'app-application-review',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './application-review.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ApplicationReviewComponent implements OnInit {
  applicationId!: number;
  application: Application | null = null;
  mailForm!: FormGroup;
  statusEnum = ApplicationStatus;
  
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private applicationService: ApplicationService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Récupération de l'ID via l'URL
    this.route.paramMap.subscribe(params => {
      const id = params.get('applicationId');
      if (id) {
        this.applicationId = +id;
        this.loadApplication();
      } else {
        this.errorMessage = "ID de candidature manquant.";
      }
    });
  }

  loadApplication(): void {
    this.isLoading = true;
    this.applicationService.getApplicationById(this.applicationId).subscribe({
      next: (app) => {
        this.application = app;
        this.isLoading = false;
        this.initForm();
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de la candidature', err);
        this.errorMessage = "Impossible de charger la candidature.";
        this.isLoading = false;
        this.cdr.markForCheck();
      }
    });
  }

  initForm(): void {
    const defaultSujet = this.application?.offer?.nomEntreprise 
      ? `Candidature - ${this.application.offer.titre} chez ${this.application.offer.nomEntreprise}`
      : 'Candidature au poste de développeur';
      
    const defaultCorps = `Bonjour,\n\nJe vous contacte suite à votre annonce pour le poste de ${this.application?.offer?.titre}.\n\nMon profil correspond particulièrement à vos attentes, notamment de par mon expérience sur des projets tels que ${this.application?.projects[0]?.nom || 'ceux détaillés dans mon CV'}.\n\nVous trouverez ci-joint mon CV détaillé.\n\nCordialement,\nNadia`;

    this.mailForm = this.fb.group({
      destinataireMail: ['', [Validators.required, Validators.email]],
      sujetMail: [this.application?.sujetMail || defaultSujet, [Validators.required, Validators.minLength(5)]],
      corpsMail: [this.application?.corpsMail || defaultCorps, [Validators.required, Validators.minLength(20)]]
    });
  }

  onSaveDraft(): void {
    if (this.mailForm.valid && this.application) {
      this.isLoading = true;
      const { sujetMail, corpsMail } = this.mailForm.value;
      this.applicationService.updateDocuments(this.application.id, sujetMail, corpsMail).subscribe({
        next: (updatedApp) => {
          this.application = updatedApp;
          this.successMessage = "Brouillon sauvegardé avec succès.";
          this.isLoading = false;
          setTimeout(() => { this.successMessage = ''; this.cdr.markForCheck(); }, 3000);
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erreur de sauvegarde', err);
          this.errorMessage = "Échec de la sauvegarde.";
          this.isLoading = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.mailForm.markAllAsTouched();
    }
  }

  onApprove(): void {
    if (this.application && this.mailForm.valid) {
      this.isLoading = true;
      const payload = {
        destinataireMail: this.mailForm.value.destinataireMail,
        sujetMail: this.mailForm.value.sujetMail,
        corpsMail: this.mailForm.value.corpsMail
      };
      
      this.applicationService.sendApplication(this.application.id, payload).subscribe({
        next: (updatedApp) => {
          this.application = updatedApp;
          this.successMessage = "Candidature confirmée et email envoyé avec succès !";
          this.isLoading = false;
          this.cdr.markForCheck();
          setTimeout(() => {
            this.router.navigate(['/dashboard']);
          }, 2000);
        },
        error: (err) => {
          console.error('Erreur lors de l\'envoi de la candidature', err);
          this.errorMessage = "Erreur lors de l'envoi de l'email.";
          this.isLoading = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.mailForm.markAllAsTouched();
    }
  }

  onReject(): void {
    if (this.application) {
      this.isLoading = true;
      this.applicationService.updateStatus(this.application.id, ApplicationStatus.REJECTED).subscribe({
        next: (updatedApp) => {
          this.application = updatedApp;
          this.isLoading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Erreur rejet', err);
          this.errorMessage = "Impossible de rejeter la candidature.";
          this.isLoading = false;
          this.cdr.markForCheck();
        }
      });
    }
  }
}

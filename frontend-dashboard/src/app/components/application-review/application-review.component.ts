import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { Application } from '../../models/application.model';
import { ApplicationStatus } from '../../models/application-status.enum';

@Component({
  selector: 'app-application-review',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './application-review.component.html'
})
export class ApplicationReviewComponent implements OnInit {
  @Input() applicationId!: number;
  application: Application | null = null;
  mailForm!: FormGroup;
  statusEnum = ApplicationStatus;

  constructor(
    private applicationService: ApplicationService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // Par défaut 1 pour l'exemple, serait récupéré via ActivatedRoute
    this.applicationId = this.applicationId || 1;
    this.loadApplication();
  }

  loadApplication(): void {
    this.applicationService.getApplicationById(this.applicationId).subscribe({
      next: (app) => {
        this.application = app;
        this.initForm();
      },
      error: (err) => console.error('Erreur lors de la récupération de la candidature', err)
    });
  }

  initForm(): void {
    this.mailForm = this.fb.group({
      sujetMail: [this.application?.sujetMail || '', Validators.required],
      corpsMail: [this.application?.corpsMail || '', Validators.required]
    });
  }

  onSaveDraft(): void {
    if (this.mailForm.valid && this.application) {
      const { sujetMail, corpsMail } = this.mailForm.value;
      this.applicationService.updateDocuments(this.application.id, sujetMail, corpsMail).subscribe({
        next: (updatedApp) => {
          this.application = updatedApp;
          alert('Brouillon de l\'email sauvegardé avec succès !');
        },
        error: (err) => console.error('Erreur de sauvegarde', err)
      });
    }
  }

  onApprove(): void {
    if (this.application) {
      this.applicationService.updateStatus(this.application.id, ApplicationStatus.APPROVED).subscribe({
        next: (updatedApp) => this.application = updatedApp,
        error: (err) => console.error('Erreur approbation', err)
      });
    }
  }

  onReject(): void {
    if (this.application) {
      this.applicationService.updateStatus(this.application.id, ApplicationStatus.REJECTED).subscribe({
        next: (updatedApp) => this.application = updatedApp,
        error: (err) => console.error('Erreur rejet', err)
      });
    }
  }
}

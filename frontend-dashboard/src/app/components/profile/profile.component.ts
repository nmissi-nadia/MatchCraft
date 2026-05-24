import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProfileService, UserProfile } from '../../services/profile.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  skills: string[] = [];
  isSyncing = false;
  successMessage = '';
  errorMessage = '';
  githubProjects: any[] = [];

  constructor(private fb: FormBuilder, private profileService: ProfileService) {
    this.profileForm = this.fb.group({
      prenom: ['', Validators.required],
      nom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      titreProfessionnel: ['', Validators.required],
      githubUsername: [''],
      cvTextuel: ['']
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.profileForm.patchValue(profile);
        this.githubProjects = profile.projects || [];
        if (profile.competencesBase) {
          try {
            this.skills = JSON.parse(profile.competencesBase);
          } catch (e) {
            this.skills = [];
          }
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement du profil', err);
      }
    });
  }

  addSkill(event: any): void {
    const value = event.target.value.trim();
    if (value && !this.skills.includes(value)) {
      this.skills.push(value);
      event.target.value = '';
    }
  }

  removeSkill(skill: string): void {
    this.skills = this.skills.filter(s => s !== skill);
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      const profileData: UserProfile = {
        ...this.profileForm.value,
        competencesBase: JSON.stringify(this.skills)
      };
      
      this.profileService.updateProfile(profileData).subscribe({
        next: () => {
          this.successMessage = 'Profil sauvegardé avec succès !';
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: () => this.errorMessage = 'Erreur lors de la sauvegarde.'
      });
    }
  }

  syncGithub(): void {
    if (!this.profileForm.get('githubUsername')?.value) {
      this.errorMessage = 'Veuillez saisir un pseudo GitHub avant de synchroniser.';
      return;
    }

    if (!this.profileForm.valid) {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires du profil avant de synchroniser.';
      this.profileForm.markAllAsTouched();
      return;
    }

    const profileData: UserProfile = {
      ...this.profileForm.value,
      competencesBase: JSON.stringify(this.skills)
    };

    this.isSyncing = true;
    this.errorMessage = '';

    this.profileService.updateProfile(profileData).subscribe({
      next: () => {
        this.profileService.syncGithub().subscribe({
          next: () => {
            this.successMessage = 'Profil sauvegardé et projets GitHub synchronisés !';
            this.isSyncing = false;
            this.loadProfile();
            setTimeout(() => this.successMessage = '', 3000);
          },
          error: () => {
            this.errorMessage = 'Profil sauvegardé, mais échec de la synchronisation GitHub.';
            this.isSyncing = false;
          }
        });
      },
      error: () => {
        this.errorMessage = 'Erreur lors de la sauvegarde du profil avant synchronisation.';
        this.isSyncing = false;
      }
    });
  }
}

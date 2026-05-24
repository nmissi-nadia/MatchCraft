import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ApplicationReviewComponent } from './components/application-review/application-review.component';
import { ProfileComponent } from './components/profile/profile.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'review/:applicationId', component: ApplicationReviewComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', redirectTo: '' }
];

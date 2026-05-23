import { ApplicationStatus } from './application-status.enum';
import { Offer } from './offer.model';
import { Project } from './project.model';

export interface Application {
  id: number;
  statut: ApplicationStatus;
  cheminCvGenere: string;
  sujetMail: string;
  corpsMail: string;
  dateEnvoi: string;
  offer: Offer;
  projects: Project[];
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

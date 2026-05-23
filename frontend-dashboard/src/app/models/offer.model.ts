export interface Offer {
  id: number;
  titre: string;
  nomEntreprise: string;
  localisation: string;
  url: string;
  scorePertinence: number;
  descriptionBrute?: string;
}

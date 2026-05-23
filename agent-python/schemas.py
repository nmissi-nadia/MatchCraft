from pydantic import BaseModel, HttpUrl
from typing import Optional, Dict, Any

class ProjectCreate(BaseModel):
    github_id: str
    nom: str
    description: Optional[str] = None
    url: str
    langages: Optional[Dict[str, Any]] = None
    contenu_readme: Optional[str] = None
    user_id: int

class OfferCreate(BaseModel):
    titre: str
    nom_entreprise: Optional[str] = None
    localisation: Optional[str] = None
    description_brute: Optional[str] = None
    url: str
    plateforme_source: Optional[str] = None
    score_pertinence: Optional[float] = None

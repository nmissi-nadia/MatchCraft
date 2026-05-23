from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from schemas import ProjectCreate, OfferCreate
from services import upsert_project, insert_offer_ignore

router = APIRouter(prefix="/agent", tags=["agent"])

class SyncRequest(BaseModel):
    token: str
    user_id: int

@router.post("/sync-github")
async def sync_github(request: SyncRequest):
    # Simulation d'un traitement d'API GitHub
    fake_project = ProjectCreate(
        github_id="repo-12345",
        nom="matchcraft-demo",
        description="A demo project for MatchCraft",
        url="https://github.com/demo/matchcraft-demo",
        langages={"Python": 80, "Java": 20},
        contenu_readme="This is an awesome project.",
        user_id=request.user_id
    )
    
    try:
        project_id = await upsert_project(fake_project)
        return {"status": "success", "message": "Projet GitHub synchronisé.", "project_id": project_id}
    except Exception as e:
        raise HTTPException(status_code=500, detail="Échec de l'insertion en base de données.")

@router.post("/scrape-trigger")
async def scrape_trigger(offer: OfferCreate):
    try:
        offer_id = await insert_offer_ignore(offer)
        if offer_id:
            return {"status": "success", "message": "Nouvelle offre insérée.", "offer_id": offer_id}
        else:
            return {"status": "success", "message": "L'offre existe déjà (ignorée)."}
    except Exception as e:
        raise HTTPException(status_code=500, detail="Échec de l'insertion en base de données.")

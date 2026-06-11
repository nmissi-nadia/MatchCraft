from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from schemas import ProjectCreate, OfferCreate
from services import upsert_project, insert_offer_ignore, create_mock_application

router = APIRouter(prefix="/agent", tags=["agent"])

import requests
import asyncio

class SyncRequest(BaseModel):
    user_id: int
    github_username: str

@router.post("/sync-github")
async def sync_github(request: SyncRequest):
    url = f"https://api.github.com/users/{request.github_username}/repos?type=owner&sort=updated"
    try:
        response = await asyncio.to_thread(requests.get, url)
        if response.status_code != 200:
            raise HTTPException(status_code=404, detail="Utilisateur GitHub introuvable.")
        
        repos = response.json()
        inserted_count = 0
        
        for repo in repos:
            if not repo["fork"]:
                project = ProjectCreate(
                    github_id=str(repo["id"]),
                    nom=repo["name"],
                    description=repo["description"] or "",
                    url=repo["html_url"],
                    langages={repo["language"]: 100} if repo["language"] else {},
                    contenu_readme="",
                    user_id=request.user_id
                )
                await upsert_project(project)
                inserted_count += 1
                
        return {"status": "success", "message": f"{inserted_count} projets synchronisés."}
    except Exception as e:
        print(f"Error sync: {e}")
        raise HTTPException(status_code=500, detail="Échec de la synchronisation.")

@router.post("/scrape-trigger")
async def scrape_trigger(offer: OfferCreate):
    try:
        offer_id = await insert_offer_ignore(offer)
        
        if not offer_id:
            return {"status": "processed", "application_created": False, "message": "Offer already exists."}
            
        app_created = False
        
        if offer.estimated_relevance_score and offer.estimated_relevance_score >= 0.80:
            app_id = await create_mock_application(
                offer=offer,
                offer_id=offer_id, 
                user_id=1
            )
            if app_id:
                app_created = True
        
        return {"status": "processed", "application_created": app_created}
    except Exception as e:
        print(f"Error scrape: {e}")
        raise HTTPException(status_code=500, detail="Erreur interne lors du traitement de l'offre.")

class ScrapeRequest(BaseModel):
    keyword: str
    user_id: int = 1

from scraper import scrape_rekrute_jobs

@router.post("/run-scraper")
async def run_scraper(request: ScrapeRequest):
    try:
        offers = await asyncio.to_thread(scrape_rekrute_jobs, request.keyword)
        
        inserted_count = 0
        applications_created = 0
        
        for offer in offers:
            # 1. Inserer l'offre
            offer_id = await insert_offer_ignore(offer)
            if offer_id:
                inserted_count += 1
                # 2. Si le score est bon, generer la candidature
                if offer.estimated_relevance_score and offer.estimated_relevance_score >= 0.80:
                    app_id = await create_mock_application(
                        offer=offer,
                        offer_id=offer_id, 
                        user_id=request.user_id
                    )
                    if app_id:
                        applications_created += 1
                        
        return {
            "status": "success",
            "offers_found": len(offers),
            "offers_inserted": inserted_count,
            "applications_created": applications_created
        }
    except Exception as e:
        print(f"Error run_scraper: {e}")
        raise HTTPException(status_code=500, detail="Échec du scraping.")

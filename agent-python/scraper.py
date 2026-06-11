from schemas import OfferCreate
from typing import List
from jobspy import scrape_jobs
import pandas as pd
from datetime import datetime, timedelta

def scrape_rekrute_jobs(keyword: str) -> List[OfferCreate]:
    """
    Scrape job offers from major platforms (LinkedIn, Indeed, Glassdoor)
    and filter for jobs posted in the last 30 days.
    (Function name kept as scrape_rekrute_jobs for backward compatibility with routers.py)
    """
    offers = []
    try:
        # Scrape jobs using jobspy
        # We limit the number of results per site to avoid long processing times in this demo
        jobs_df = scrape_jobs(
            site_name=["indeed", "linkedin", "glassdoor"],
            search_term=keyword,
            location="Maroc", 
            results_wanted=10, 
            hours_old=720 # 30 days * 24 hours
        )
        
        if jobs_df is None or jobs_df.empty:
            print("Aucune offre trouvée par JobSpy.")
            return offers
            
        # Convert DataFrame to list of dictionaries
        jobs = jobs_df.to_dict(orient="records")
        
        for job in jobs:
            # Extrait les informations
            titre = str(job.get("title", ""))
            nom_entreprise = str(job.get("company", "Entreprise confidentielle"))
            localisation = str(job.get("location", "Maroc"))
            description_brute = str(job.get("description", "Description non disponible."))
            if description_brute == "nan":
                description_brute = "Description non disponible."
                
            link = str(job.get("job_url", ""))
            plateforme_source = str(job.get("site", "JobSpy"))
            
            # Simulation d'un score de pertinence via l'IA
            estimated_relevance_score = 0.85
            
            offer = OfferCreate(
                titre=titre,
                nom_entreprise=nom_entreprise if nom_entreprise != "nan" else "Entreprise confidentielle",
                localisation=localisation if localisation != "nan" else "Maroc",
                description_brute=description_brute[:2000] + "..." if len(description_brute) > 2000 else description_brute,
                url=link,
                plateforme_source=plateforme_source,
                estimated_relevance_score=estimated_relevance_score
            )
            offers.append(offer)
            
    except Exception as e:
        print(f"Erreur lors du scraping JobSpy: {e}")
        
    return offers

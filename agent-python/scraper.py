import requests
from bs4 import BeautifulSoup
from schemas import OfferCreate
from typing import List

def scrape_rekrute_jobs(keyword: str) -> List[OfferCreate]:
    """
    Scrape job offers from rekrute.com (focusing on jobs in Morocco).
    """
    url = f"https://www.rekrute.com/offres.html?keyword={keyword}"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    
    offers = []
    
    try:
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Trouver les blocs d'offres sur rekrute (généralement class "post-id")
        job_cards = soup.find_all('li', class_='post-id')
        
        for card in job_cards[:5]:  # On limite aux 5 premières pour l'exemple
            # Titre de l'offre
            title_tag = card.find('h2')
            if not title_tag or not title_tag.find('a'):
                continue
            titre = title_tag.find('a').text.strip()
            link = "https://www.rekrute.com" + title_tag.find('a')['href']
            
            # Nom de l'entreprise
            company_tag = card.find('img', class_='logoCompany')
            nom_entreprise = company_tag['alt'].strip() if company_tag and 'alt' in company_tag.attrs else "Entreprise confidentielle"
            
            # Localisation
            info_tag = card.find('div', class_='info')
            localisation = "Maroc"
            if info_tag:
                loc_span = info_tag.find_all('span')
                if len(loc_span) > 1:
                    localisation = loc_span[1].text.strip() # Souvent le 2eme span contient la ville
            
            # Pour garantir qu'on est au Maroc, on peut forcer le tag s'il n'est pas précis, 
            # Rekrute est majoritairement marocain.
            if "Maroc" not in localisation and not localisation:
                localisation = "Casablanca, Maroc"
                
            # Description brute courte
            desc_tag = card.find('div', class_='intro')
            description_brute = desc_tag.text.strip() if desc_tag else "Description non disponible."
            
            # Simulation d'un score de pertinence via l'IA (en réalité, on ferait un appel LLM ici)
            # Pour le pipeline, on met un score arbitraire > 0.80 pour forcer la création de candidature
            estimated_relevance_score = 0.85
            
            offer = OfferCreate(
                titre=titre,
                nom_entreprise=nom_entreprise,
                localisation=localisation,
                description_brute=description_brute,
                url=link,
                plateforme_source="Rekrute",
                estimated_relevance_score=estimated_relevance_score
            )
            offers.append(offer)
            
    except Exception as e:
        print(f"Erreur lors du scraping de Rekrute: {e}")
        
    return offers

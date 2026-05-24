import requests
import json
import time

def test_scrape_trigger():
    url = "http://localhost:8000/agent/scrape-trigger"
    
    # Payload d'une offre réaliste
    payload = {
        "titre": "Développeur Full-Stack Java/Angular",
        "nom_entreprise": "Morocco Tech",
        "localisation": "Casablanca",
        "description_brute": "Nous recherchons un développeur Full-Stack expérimenté maîtrisant l'écosystème Spring (Spring Boot) et Angular pour rejoindre notre équipe agile.",
        "url": f"https://www.linkedin.com/jobs/view/morocco-tech-{int(time.time())}",
        "plateforme_source": "LinkedIn",
        "estimated_relevance_score": 0.85
    }

    headers = {
        "Content-Type": "application/json"
    }

    print(f"Envoi d'une requête POST vers {url}")
    print(f"Payload: {json.dumps(payload, indent=2, ensure_ascii=False)}")
    
    try:
        response = requests.post(url, json=payload, headers=headers)
        
        print("\n--- Résultat de la requête ---")
        print(f"Status Code: {response.status_code}")
        
        try:
            print(f"Réponse JSON: {json.dumps(response.json(), indent=2, ensure_ascii=False)}")
        except ValueError:
            print(f"Réponse brute: {response.text}")
            
    except requests.exceptions.ConnectionError:
        print("Erreur de connexion. Vérifiez que le conteneur agent-python est bien lancé sur le port 8000.")

if __name__ == "__main__":
    test_scrape_trigger()

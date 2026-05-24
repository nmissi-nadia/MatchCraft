import requests
import json

# L'API Spring Boot tourne sur le port 8080 (mappé depuis Docker)
URL = "http://matchcraft-backend-spring:8080/api/offers/ingest"

# Payload réaliste simulant les données renvoyées par l'agent Python après scraping et matching
payload = {
    "userId": 1,
    "titre": "Développeur Full-Stack Java / Angular",
    "nomEntreprise": "TechSolutions Maroc",
    "localisation": "Casablanca, Maroc (Hybride)",
    "descriptionBrute": "Nous recherchons un développeur Full-Stack passionné maitrisant Spring Boot 3 et Angular 17. Le candidat idéal devrait avoir une expérience dans les architectures microservices et Docker.",
    "url": "https://www.linkedin.com/jobs/view/fake-id-12345",
    "plateformeSource": "LinkedIn",
    "scorePertinence": 0.85,
    "projectIds": [1, 2] # ID fictifs de vos projets GitHub (ex: un backend Java, un frontend Angular)
}

print("Envoi de la requête d'ingestion au Backend Java...")

try:
    response = requests.post(URL, json=payload, headers={"Content-Type": "application/json"})
    
    if response.status_code == 200:
        print("✅ Succès ! L'offre a été ingérée et la candidature générée.")
        print(json.dumps(response.json(), indent=2))
    else:
        print(f"❌ Erreur {response.status_code}: {response.text}")
except requests.exceptions.ConnectionError:
    print("❌ Impossible de se connecter. Vérifiez que le backend Spring Boot tourne bien dans Docker sur le port 8080.")

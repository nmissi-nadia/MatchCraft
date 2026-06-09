import requests
import json
import time

BASE_URL = "http://localhost:8080/api"

def print_result(step_name, response):
    print(f"\n--- {step_name} ---")
    print(f"Status Code: {response.status_code}")
    try:
        print(json.dumps(response.json(), indent=2, ensure_ascii=False))
    except ValueError:
        print(response.text)

def test_api_workflow():
    print("Démarrage des tests de l'API MatchCraft...")

    # 1. Ingest an offer
    offer_payload = {
        "userId": 1,
        "titre": "Développeur Full-Stack Java / Angular",
        "nomEntreprise": "TechSolutions Maroc",
        "localisation": "Casablanca, Maroc (Hybride)",
        "descriptionBrute": "Nous recherchons un développeur Full-Stack passionné maitrisant Spring Boot 3 et Angular 17.",
        "url": f"https://www.linkedin.com/jobs/view/fake-id-{int(time.time())}",
        "plateformeSource": "LinkedIn",
        "scorePertinence": 0.85,
        "projectIds": [1, 2] # Ensure these IDs exist in DB or adjust
    }

    try:
        response = requests.post(f"{BASE_URL}/offers/ingest", json=offer_payload, headers={"Content-Type": "application/json"})
        print_result("1. Ingestion d'une offre", response)
        
        # 2. Get applications (to verify creation)
        response = requests.get(f"{BASE_URL}/applications")
        print_result("2. Récupération des candidatures", response)

        applications = response.json()
        if "content" in applications and len(applications["content"]) > 0:
            app_id = applications["content"][0]["id"]
            
            # 3. Get specific application
            response = requests.get(f"{BASE_URL}/applications/{app_id}")
            print_result(f"3. Récupération de la candidature {app_id}", response)

            # 4. Update status
            status_payload = {"statut": "ACCEPTED"} # Adjust based on your enum
            response = requests.patch(f"{BASE_URL}/applications/{app_id}/status", json=status_payload)
            print_result("4. Mise à jour du statut", response)

    except requests.exceptions.ConnectionError:
        print("\n❌ Impossible de se connecter. Vérifiez que le backend Spring Boot tourne bien sur le port 8080.")

if __name__ == "__main__":
    test_api_workflow()

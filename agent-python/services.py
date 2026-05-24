import json
from database import get_db_connection
from schemas import ProjectCreate, OfferCreate
import asyncpg

async def upsert_project(project: ProjectCreate):
    query = """
        INSERT INTO projects (github_id, nom, description, url, langages, contenu_readme, user_id)
        VALUES ($1, $2, $3, $4, $5::jsonb, $6, $7)
        ON CONFLICT (github_id) DO UPDATE SET
            nom = EXCLUDED.nom,
            description = EXCLUDED.description,
            url = EXCLUDED.url,
            langages = EXCLUDED.langages,
            contenu_readme = EXCLUDED.contenu_readme,
            user_id = EXCLUDED.user_id
        RETURNING id;
    """
    langages_json = json.dumps(project.langages) if project.langages else None
    
    try:
        async with get_db_connection() as conn:
            project_id = await conn.fetchval(
                query,
                project.github_id,
                project.nom,
                project.description,
                project.url,
                langages_json,
                project.contenu_readme,
                project.user_id
            )
            return project_id
    except asyncpg.PostgresError as e:
        print(f"Erreur SQL lors de l'upsert du projet: {e}")
        raise

async def insert_offer_ignore(offer: OfferCreate):
    query = """
        INSERT INTO offers (titre, nom_entreprise, localisation, description_brute, url, plateforme_source, score_pertinence)
        VALUES ($1, $2, $3, $4, $5, $6, $7)
        ON CONFLICT (url) DO NOTHING
        RETURNING id;
    """
    try:
        async with get_db_connection() as conn:
            # fetchval retourne None si la clause DO NOTHING empêche l'insertion
            offer_id = await conn.fetchval(
                query,
                offer.titre,
                offer.nom_entreprise,
                offer.localisation,
                offer.description_brute,
                offer.url,
                offer.plateforme_source,
                offer.estimated_relevance_score
            )
            return offer_id
    except asyncpg.PostgresError as e:
        print(f"Erreur SQL lors de l'insertion de l'offre: {e}")
        raise

async def create_mock_application(offer_id: int, offer_title: str, company_name: str, user_id: int = 1):
    query = """
        INSERT INTO applications (offer_id, user_id, statut, chemin_cv_genere, sujet_mail, corps_mail, date_envoi)
        VALUES ($1, $2, $3, $4, $5, $6, CURRENT_TIMESTAMP)
        RETURNING id;
    """
    
    generated_cv_path = f"/storage/cv_generated_{user_id}.pdf"
    generated_email_subject = f"Candidature - {offer_title} - Tech Profile"
    generated_email_body = f"Bonjour,\n\nJe vous écris pour postuler au poste de {offer_title} au sein de {company_name}. Je suis convaincu que mon profil correspond à vos attentes."
    
    try:
        async with get_db_connection() as conn:
            # Check if user exists
            user_exists = await conn.fetchval("SELECT 1 FROM users WHERE id = $1", user_id)
            if not user_exists:
                print(f"Utilisateur {user_id} introuvable. Impossible de créer la candidature.")
                return None

            application_id = await conn.fetchval(
                query,
                offer_id,
                user_id,
                'PENDING_REVIEW',
                generated_cv_path,
                generated_email_subject,
                generated_email_body
            )
            return application_id
    except asyncpg.PostgresError as e:
        print(f"Erreur SQL lors de la création de la candidature: {e}")
        raise

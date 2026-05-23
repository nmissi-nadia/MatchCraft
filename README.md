# MatchCraft : Système Automatisé de Veille et d'Ajustement des Candidatures

## Présentation Générale

MatchCraft est une application d'automatisation de recherche d'emploi et de gestion de candidatures conçue selon l'approche "Human-in-the-loop" (validation humaine intégrée). Le système automatise la collecte des offres, l'analyse de leur pertinence par rapport à un portefeuille de projets GitHub, ainsi que la génération contextuelle des CV et des courriels d'accompagnement, tout en maintenant une phase de validation finale obligatoire par l'utilisateur.

## Architecture Système et Microservices

L'application repose sur une architecture découplée, modulaire et entièrement conteneurisée sous Docker, structurée de la manière suivante :

* **`agent-python` (FastAPI) :** Module d'acquisition de données et de traitement algorithmique. Il prend en charge le scraping des plateformes d'emploi (LinkedIn, Rekrute), l'interconnexion avec l'API GitHub (`PyGithub`) pour extraire et analyser la documentation technique des dépôts (fichiers README.md), et le matching sémantique (NLP) permettant d'évaluer le score d'adéquation entre l'offre et le profil.
* **`backend-spring` (Spring Boot) :** Noyau fonctionnel et logique métier de l'application. Il assure la persistance des données, la gestion des états du pipeline de validation, la génération dynamique des fichiers CV au format PDF et l'interfaçage avec les protocoles d'envoi de courriels (SMTP/API).
* **`frontend-dashboard` :** Interface de supervision permettant à l'utilisateur de passer en revue les propositions de candidatures, d'éditer les documents générés à la volée, de suivre les indicateurs de performance et de déclencher les envois.
* **Base de Données (PostgreSQL) :** Système de gestion de base de données relationnelle dédié au traçage complet, à l'historisation des interactions avec les entreprises et à la persistance des indicateurs de suivi.

## Technologies Utilisées

* **Environnement Backend :** Java, Spring Boot, Spring Data JPA, Spring Security
* **Analyse de Données et IA :** Python, FastAPI, PyGithub, Traitement Automatique du Langage Naturel (NLP)
* **Système de Stockage :** PostgreSQL
* **Infrastructure et Déploiement :** Docker, Docker Compose
* **Environnement Frontend :** TypeScript, Tailwind CSS

---
*Projet conçu et développé dans le cadre d'une recherche d'opportunités ciblées et d'optimisation de portfolio technique.*

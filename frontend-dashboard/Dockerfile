# Étape 1 : Build de l'application Angular avec Node.js
FROM node:20-alpine AS builder

WORKDIR /app

# On copie les fichiers de configuration des dépendances
# S'ils ne sont pas encore créés (projet vide), le build échouera, il faudra générer le projet Angular d'abord.
COPY package.json package-lock.json* ./

# Installation des dépendances
RUN npm install

# Copie du reste du code source
COPY . .

# Construction de l'application pour la production
RUN npm run build --configuration=production

# Étape 2 : Serveur Web Nginx ultra-léger
FROM nginx:alpine

# Copie des fichiers compilés depuis le builder vers Nginx
# Attention: Le chemin /app/dist/frontend-dashboard/browser dépend de la version d'Angular (ici v17+)
COPY --from=builder /app/dist/frontend-dashboard/browser /usr/share/nginx/html

# Optionnel mais recommandé pour Angular : copier une configuration nginx.conf pour gérer le fallback des routes (SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

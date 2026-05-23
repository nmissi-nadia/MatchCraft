# Étape 1 : Build avec Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copie du pom.xml pour télécharger les dépendances (mise en cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copie des sources et compilation sans exécuter les tests
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution allégée
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copie du .jar généré par l'étape précédente
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

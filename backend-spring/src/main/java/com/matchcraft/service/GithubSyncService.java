package com.matchcraft.service;

import com.matchcraft.domain.entity.Project;
import com.matchcraft.domain.entity.User;
import com.matchcraft.repository.ProjectRepository;
import com.matchcraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubSyncService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${GITHUB_USERNAME:nmissi-nadia}")
    private String githubUsername;

    public GithubSyncService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public void syncProjectsFromGithub() {
        System.out.println("Starting GitHub projects sync for user: " + githubUsername);
        
        // Ensure default user exists or fetch them
        User user = userRepository.findById(1L).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(1L);
            newUser.setPrenom("Nadia");
            newUser.setNom("NMISSI");
            newUser.setEmail("nmissinadia@gmail.com");
            newUser.setTitreProfessionnel("Développeur Full-Stack");
            newUser.setCompetencesBase("[\"Java\", \"Angular\", \"Spring Boot\", \"React\"]");
            return userRepository.save(newUser);
        });

        String url = "https://api.github.com/users/" + githubUsername + "/repos?per_page=100";

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> repos = response.getBody();
            if (repos != null) {
                int count = 0;
                for (Map<String, Object> repo : repos) {
                    String githubId = String.valueOf(repo.get("id"));
                    String nom = (String) repo.get("name");
                    String description = (String) repo.get("description");
                    String htmlUrl = (String) repo.get("html_url");
                    String language = (String) repo.get("language");

                    Project project = projectRepository.findByGithubId(githubId);
                    if (project == null) {
                        project = new Project();
                        project.setGithubId(githubId);
                        project.setUser(user);
                    }

                    project.setNom(nom != null ? nom : "Sans nom");
                    project.setDescription(description != null ? description : "Aucune description");
                    project.setUrl(htmlUrl);
                    
                    if (language != null) {
                        project.setLangages("[\"" + language + "\"]");
                    } else {
                        project.setLangages("[]");
                    }

                    projectRepository.save(project);
                    count++;
                }
                System.out.println("Successfully synced " + count + " projects from GitHub.");
            }
        } catch (Exception e) {
            System.err.println("Error syncing GitHub projects: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

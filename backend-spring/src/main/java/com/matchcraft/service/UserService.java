package com.matchcraft.service;

import com.matchcraft.domain.entity.Project;
import com.matchcraft.domain.entity.User;
import com.matchcraft.dto.ProjectDto;
import com.matchcraft.dto.UserDto;
import com.matchcraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${agent.python.url:http://agent-python:8000}")
    private String agentPythonUrl;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return mapToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        
        user.setPrenom(dto.getPrenom());
        user.setNom(dto.getNom());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setTitreProfessionnel(dto.getTitreProfessionnel());
        user.setCompetencesBase(dto.getCompetencesBase());
        user.setGithubUsername(dto.getGithubUsername());
        user.setCvTextuel(dto.getCvTextuel());

        user = userRepository.save(user);
        return mapToDto(user);
    }

    public void syncGithub(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        if (user.getGithubUsername() == null || user.getGithubUsername().isEmpty()) {
            throw new RuntimeException("Le pseudo GitHub n'est pas renseigné");
        }
        
        String url = agentPythonUrl + "/agent/sync-github";
        // Appel au service Python pour faire le sync réel
        // payload: {"user_id": 1, "github_username": "..."}
        
        // On construit l'objet de requete simple
        java.util.Map<String, Object> request = new java.util.HashMap<>();
        request.put("user_id", id);
        request.put("github_username", user.getGithubUsername());

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erreur lors de la synchronisation via l'agent Python");
        }
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setPrenom(user.getPrenom());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setTitreProfessionnel(user.getTitreProfessionnel());
        dto.setCompetencesBase(user.getCompetencesBase());
        dto.setGithubUsername(user.getGithubUsername());
        dto.setCvTextuel(user.getCvTextuel());
        
        if (user.getProjects() != null) {
            List<ProjectDto> projectDtos = user.getProjects().stream().map(p -> {
                ProjectDto pdto = new ProjectDto();
                pdto.setId(p.getId());
                pdto.setNom(p.getNom());
                pdto.setDescription(p.getDescription());
                pdto.setUrl(p.getUrl());
                pdto.setGithubId(p.getGithubId());
                return pdto;
            }).collect(Collectors.toList());
            dto.setProjects(projectDtos);
        }
        
        return dto;
    }
}

package com.matchcraft.dto;

import java.util.List;

public class UserDto {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String titreProfessionnel;
    private String competencesBase;
    private String githubUsername;
    private String cvTextuel;
    private List<ProjectDto> projects;

    public UserDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTitreProfessionnel() { return titreProfessionnel; }
    public void setTitreProfessionnel(String titreProfessionnel) { this.titreProfessionnel = titreProfessionnel; }

    public String getCompetencesBase() { return competencesBase; }
    public void setCompetencesBase(String competencesBase) { this.competencesBase = competencesBase; }

    public String getGithubUsername() { return githubUsername; }
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }

    public String getCvTextuel() { return cvTextuel; }
    public void setCvTextuel(String cvTextuel) { this.cvTextuel = cvTextuel; }

    public List<ProjectDto> getProjects() { return projects; }
    public void setProjects(List<ProjectDto> projects) { this.projects = projects; }
}

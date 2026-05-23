package com.matchcraft.dto;

public class ProjectDto {
    private Long id;
    private String githubId;
    private String nom;
    private String description;
    private String url;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGithubId() { return githubId; }
    public void setGithubId(String githubId) { this.githubId = githubId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

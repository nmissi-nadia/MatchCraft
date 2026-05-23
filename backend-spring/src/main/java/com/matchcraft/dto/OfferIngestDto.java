package com.matchcraft.dto;

import java.util.List;

public class OfferIngestDto {
    private String titre;
    private String nomEntreprise;
    private String localisation;
    private String descriptionBrute;
    private String url;
    private String plateformeSource;
    private Double scorePertinence;
    private Long userId;
    private List<Long> projectIds;

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getDescriptionBrute() { return descriptionBrute; }
    public void setDescriptionBrute(String descriptionBrute) { this.descriptionBrute = descriptionBrute; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getPlateformeSource() { return plateformeSource; }
    public void setPlateformeSource(String plateformeSource) { this.plateformeSource = plateformeSource; }
    public Double getScorePertinence() { return scorePertinence; }
    public void setScorePertinence(Double scorePertinence) { this.scorePertinence = scorePertinence; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<Long> getProjectIds() { return projectIds; }
    public void setProjectIds(List<Long> projectIds) { this.projectIds = projectIds; }
}

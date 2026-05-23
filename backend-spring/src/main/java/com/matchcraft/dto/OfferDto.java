package com.matchcraft.dto;

public class OfferDto {
    private Long id;
    private String titre;
    private String nomEntreprise;
    private String localisation;
    private String url;
    private Double scorePertinence;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Double getScorePertinence() { return scorePertinence; }
    public void setScorePertinence(Double scorePertinence) { this.scorePertinence = scorePertinence; }
}

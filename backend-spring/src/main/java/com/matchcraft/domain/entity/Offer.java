package com.matchcraft.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(name = "nom_entreprise")
    private String nomEntreprise;

    private String localisation;

    @Column(name = "description_brute", columnDefinition = "TEXT")
    private String descriptionBrute;

    @Column(unique = true, nullable = false)
    private String url;

    @Column(name = "plateforme_source")
    private String plateformeSource;

    @Column(name = "score_pertinence")
    private Double scorePertinence;

    @OneToOne(mappedBy = "offer")
    private Application application;

    public Offer() {}

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
}

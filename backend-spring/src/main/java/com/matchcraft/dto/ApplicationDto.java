package com.matchcraft.dto;

import com.matchcraft.domain.enums.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationDto {
    private Long id;
    private ApplicationStatus statut;
    private String cheminCvGenere;
    private String sujetMail;
    private String corpsMail;
    private LocalDateTime dateEnvoi;
    private OfferDto offer;
    private List<ProjectDto> projects;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ApplicationStatus getStatut() { return statut; }
    public void setStatut(ApplicationStatus statut) { this.statut = statut; }
    public String getCheminCvGenere() { return cheminCvGenere; }
    public void setCheminCvGenere(String cheminCvGenere) { this.cheminCvGenere = cheminCvGenere; }
    public String getSujetMail() { return sujetMail; }
    public void setSujetMail(String sujetMail) { this.sujetMail = sujetMail; }
    public String getCorpsMail() { return corpsMail; }
    public void setCorpsMail(String corpsMail) { this.corpsMail = corpsMail; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    public OfferDto getOffer() { return offer; }
    public void setOffer(OfferDto offer) { this.offer = offer; }
    public List<ProjectDto> getProjects() { return projects; }
    public void setProjects(List<ProjectDto> projects) { this.projects = projects; }
}

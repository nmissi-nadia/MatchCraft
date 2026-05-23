package com.matchcraft.domain.entity;

import com.matchcraft.domain.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", referencedColumnName = "id", unique = true, nullable = false)
    private Offer offer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus statut;

    @Column(name = "chemin_cv_genere")
    private String cheminCvGenere;

    @Column(name = "sujet_mail")
    private String sujetMail;

    @Column(name = "corps_mail", columnDefinition = "TEXT")
    private String corpsMail;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @ManyToMany
    @JoinTable(
        name = "application_project",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    public Application() {}

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Offer getOffer() { return offer; }
    public void setOffer(Offer offer) { this.offer = offer; }

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

    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
}

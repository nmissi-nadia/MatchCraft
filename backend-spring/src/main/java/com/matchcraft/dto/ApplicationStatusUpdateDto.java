package com.matchcraft.dto;

import com.matchcraft.domain.enums.ApplicationStatus;

public class ApplicationStatusUpdateDto {
    private ApplicationStatus statut;

    public ApplicationStatus getStatut() { return statut; }
    public void setStatut(ApplicationStatus statut) { this.statut = statut; }
}

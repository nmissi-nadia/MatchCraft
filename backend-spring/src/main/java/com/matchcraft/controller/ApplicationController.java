package com.matchcraft.controller;

import com.matchcraft.domain.entity.Application;
import com.matchcraft.domain.enums.ApplicationStatus;
import com.matchcraft.dto.*;
import com.matchcraft.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<Page<ApplicationDto>> getApplications(
            @RequestParam(required = false) ApplicationStatus statut,
            Pageable pageable) {
        Page<Application> applications = applicationService.getApplications(statut, pageable);
        return ResponseEntity.ok(applications.map(this::mapToDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(mapToDto(application));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationDto> updateStatus(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateDto dto) {
        Application application = applicationService.updateStatus(id, dto);
        return ResponseEntity.ok(mapToDto(application));
    }

    @PutMapping("/{id}/documents")
    public ResponseEntity<ApplicationDto> updateDocuments(
            @PathVariable Long id,
            @RequestBody ApplicationDocumentsUpdateDto dto) {
        Application application = applicationService.updateDocuments(id, dto);
        return ResponseEntity.ok(mapToDto(application));
    }

    private ApplicationDto mapToDto(Application app) {
        ApplicationDto dto = new ApplicationDto();
        dto.setId(app.getId());
        dto.setStatut(app.getStatut());
        dto.setCheminCvGenere(app.getCheminCvGenere());
        dto.setSujetMail(app.getSujetMail());
        dto.setCorpsMail(app.getCorpsMail());
        dto.setDateEnvoi(app.getDateEnvoi());

        if (app.getOffer() != null) {
            OfferDto offerDto = new OfferDto();
            offerDto.setId(app.getOffer().getId());
            offerDto.setTitre(app.getOffer().getTitre());
            offerDto.setNomEntreprise(app.getOffer().getNomEntreprise());
            offerDto.setLocalisation(app.getOffer().getLocalisation());
            offerDto.setUrl(app.getOffer().getUrl());
            offerDto.setScorePertinence(app.getOffer().getScorePertinence());
            dto.setOffer(offerDto);
        }

        if (app.getProjects() != null) {
            List<ProjectDto> projectDtos = app.getProjects().stream().map(p -> {
                ProjectDto pDto = new ProjectDto();
                pDto.setId(p.getId());
                pDto.setGithubId(p.getGithubId());
                pDto.setNom(p.getNom());
                pDto.setDescription(p.getDescription());
                pDto.setUrl(p.getUrl());
                return pDto;
            }).collect(Collectors.toList());
            dto.setProjects(projectDtos);
        }

        return dto;
    }
}

package com.matchcraft.service;

import com.matchcraft.domain.entity.Application;
import com.matchcraft.domain.entity.Offer;
import com.matchcraft.domain.entity.User;
import com.matchcraft.domain.enums.ApplicationStatus;
import com.matchcraft.dto.OfferIngestDto;
import com.matchcraft.exception.ResourceNotFoundException;
import com.matchcraft.repository.ApplicationRepository;
import com.matchcraft.repository.OfferRepository;
import com.matchcraft.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public OfferService(OfferRepository offerRepository, ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Application ingestOffer(OfferIngestDto dto) {
        // En prod, l'utilisateur serait lié dynamiquement, ici on force la récupération pour la démo
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User non trouvé avec l'id: " + dto.getUserId()));

        Offer offer = offerRepository.findByUrl(dto.getUrl());
        if (offer == null) {
            offer = new Offer();
            offer.setUrl(dto.getUrl());
        }
        
        offer.setTitre(dto.getTitre());
        offer.setNomEntreprise(dto.getNomEntreprise());
        offer.setLocalisation(dto.getLocalisation());
        offer.setDescriptionBrute(dto.getDescriptionBrute());
        offer.setPlateformeSource(dto.getPlateformeSource());
        offer.setScorePertinence(dto.getScorePertinence());
        
        offer = offerRepository.save(offer);

        Application application = offer.getApplication();
        if (application == null) {
            application = new Application();
            application.setOffer(offer);
            application.setUser(user);
            application.setStatut(ApplicationStatus.PENDING_REVIEW);
            application = applicationRepository.save(application);
        }

        return application;
    }
}

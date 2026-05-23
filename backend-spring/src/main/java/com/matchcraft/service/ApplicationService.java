package com.matchcraft.service;

import com.matchcraft.domain.entity.Application;
import com.matchcraft.domain.enums.ApplicationStatus;
import com.matchcraft.dto.ApplicationDocumentsUpdateDto;
import com.matchcraft.dto.ApplicationStatusUpdateDto;
import com.matchcraft.exception.ResourceNotFoundException;
import com.matchcraft.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional(readOnly = true)
    public Page<Application> getApplications(ApplicationStatus statut, Pageable pageable) {
        if (statut != null) {
            return applicationRepository.findByStatut(statut, pageable);
        }
        return applicationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application non trouvée avec l'id : " + id));
    }

    @Transactional
    public Application updateStatus(Long id, ApplicationStatusUpdateDto dto) {
        Application application = getApplicationById(id);
        application.setStatut(dto.getStatut());
        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateDocuments(Long id, ApplicationDocumentsUpdateDto dto) {
        Application application = getApplicationById(id);
        application.setSujetMail(dto.getSujetMail());
        application.setCorpsMail(dto.getCorpsMail());
        return applicationRepository.save(application);
    }
}

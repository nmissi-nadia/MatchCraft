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
    private final PdfGenerationService pdfGenerationService;
    private final EmailService emailService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              PdfGenerationService pdfGenerationService,
                              EmailService emailService) {
        this.applicationRepository = applicationRepository;
        this.pdfGenerationService = pdfGenerationService;
        this.emailService = emailService;
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
        Application app = getApplicationById(id);
        app.setSujetMail(dto.getSujetMail());
        app.setCorpsMail(dto.getCorpsMail());
        return applicationRepository.save(app);
    }

    public Application sendApplication(Long id, java.util.Map<String, String> payload) {
        Application application = getApplicationById(id);

        String finalSubject = payload.get("sujetMail");
        String finalBody = payload.get("corpsMail");
        
        if (finalSubject == null || finalSubject.trim().isEmpty() || finalSubject.length() < 5) {
            throw new IllegalArgumentException("Le sujet de l'email est invalide ou trop court.");
        }
        if (finalBody == null || finalBody.trim().isEmpty() || finalBody.length() < 20) {
            throw new IllegalArgumentException("Le corps de l'email est invalide ou trop court.");
        }

        String recipient = "test@entreprise.com"; 

        try {
            // 1. Generate PDF
            byte[] pdfBytes = pdfGenerationService.generateCvPdf(application);
            
            // 2. Send Email
            emailService.sendApplicationEmail(recipient, finalSubject, finalBody, pdfBytes);

            // 3. Update status
            application.setStatut(ApplicationStatus.SENT);
            return applicationRepository.save(application);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de la candidature", e);
        }
    }
}

package com.matchcraft.service;

import com.matchcraft.domain.entity.Application;
import com.matchcraft.domain.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGenerationService {

    private final TemplateEngine templateEngine;

    @Autowired
    public PdfGenerationService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateCvPdf(Application application) throws Exception {
        Context context = new Context();
        context.setVariable("user", application.getUser());
        context.setVariable("offer", application.getOffer());
        
        // On ne passe que les projets sélectionnés par l'IA (ou l'utilisateur)
        List<Project> selectedProjects = application.getProjects();
        context.setVariable("projects", selectedProjects);

        String htmlContent = templateEngine.process("cv-template", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, "/");
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }
}

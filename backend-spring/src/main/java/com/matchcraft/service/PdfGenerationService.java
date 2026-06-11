package com.matchcraft.service;

import com.matchcraft.domain.entity.Application;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

@Service
public class PdfGenerationService {

    public byte[] generateCvPdf(Application application) throws Exception {
        String title = application.getOffer().getTitre() != null ? application.getOffer().getTitre().toLowerCase() : "";
        String desc = application.getOffer().getDescriptionBrute() != null ? application.getOffer().getDescriptionBrute().toLowerCase() : "";
        
        String contentToAnalyze = title + " " + desc;
        
        String selectedCvFile = "NMISSI_N_CV_Full_Stack.pdf"; // Default
        
        if (contentToAnalyze.contains("php") || contentToAnalyze.contains("laravel") || contentToAnalyze.contains("symfony")) {
            selectedCvFile = "NMISSI_N_PHP_CV.pdf";
        } else if (contentToAnalyze.contains("angular")) {
            selectedCvFile = "NMISSI_CV_ANG.pdf";
        } else if (contentToAnalyze.contains("react") || contentToAnalyze.contains("frontend") || contentToAnalyze.contains("vue")) {
            selectedCvFile = "NMISSI_N_CV_RA.pdf"; 
        } else if (contentToAnalyze.contains("spring") || contentToAnalyze.contains("java") || contentToAnalyze.contains("python") || contentToAnalyze.contains("backend") || contentToAnalyze.contains("data")) {
            selectedCvFile = "NMISSI_N_CV.pdf";
        }

        try {
            java.io.File pdfFile = new java.io.File("/app/cvs/" + selectedCvFile);
            try (InputStream is = new java.io.FileInputStream(pdfFile)) {
                return StreamUtils.copyToByteArray(is);
            }
        } catch (Exception e) {
            System.err.println("Le fichier CV " + selectedCvFile + " est introuvable. On retourne un tableau vide.");
            throw new RuntimeException("CV non trouvé : " + selectedCvFile, e);
        }
    }
}

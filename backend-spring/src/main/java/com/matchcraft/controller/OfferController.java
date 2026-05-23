package com.matchcraft.controller;

import com.matchcraft.dto.OfferIngestDto;
import com.matchcraft.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<?> ingestOffer(@RequestBody OfferIngestDto dto) {
        offerService.ingestOffer(dto);
        return ResponseEntity.ok(Map.of("message", "Offer ingested and application created successfully"));
    }
}

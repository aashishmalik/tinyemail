package com.example.tinyemail.controller;

import com.example.tinyemail.enums.CampaignStatus;
import com.example.tinyemail.exceptions.InvalidFieldException;
import com.example.tinyemail.resource.CampaignResource;
import com.example.tinyemail.resource.CampaignRunResource;
import com.example.tinyemail.service.CampaignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/campaign")
@Slf4j
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping
    public ResponseEntity<
            CampaignResource> createCampaign(@RequestBody @Validated CampaignResource request,
                                             BindingResult results) {
        log.info("Calling createCampaign API for request: {}", request);
        if (results.hasErrors()) {
            List<FieldError> fieldErrors = results.getFieldErrors();
            log.error("Invalid request createCampaign API for request: {}", results.getAllErrors());
            throw new InvalidFieldException(fieldErrors);
        }
        return ResponseEntity.ok(campaignService.createCampaign(request));
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignResource> getCampaignById(@PathVariable("campaignId") String id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResource>> getCampaignsByUserId() {
        return ResponseEntity.ok(campaignService.getCampaigns());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResource> updateCampaign(@RequestBody @Valid CampaignResource resource,
        BindingResult results, @PathVariable String id) {
        log.info("Calling updateCampaign API for request: {}", resource);
        if (results.hasErrors()) {
            List<FieldError> fieldErrors = results.getFieldErrors();
            log.error("Invalid request updateCampaign API for request: {}", results.getAllErrors());
            throw new InvalidFieldException(fieldErrors);
        }
        resource.setId(id);
        return ResponseEntity.ok(campaignService.updateCampaign(resource));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<CampaignResource> publishCampaign(@PathVariable String id) {
        log.info("Calling archieve API for campaign Id: {}", id);
        return ResponseEntity.ok(campaignService.updateCampaingStatus(id, CampaignStatus.PUBLISHED));
    }

    @PostMapping("/run")
    public ResponseEntity<Void> runCampaign(@RequestBody CampaignRunResource resource) {
        campaignService.runCampaign(resource);
        return ResponseEntity.ok().build();
    }

}

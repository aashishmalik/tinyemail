package com.example.tinyemail.service;

import com.example.tinyemail.enums.CampaignStatus;
import com.example.tinyemail.exceptions.AdGenericException;
import com.example.tinyemail.model.Campaign;
import com.example.tinyemail.repository.CampaignRepositoryWrapper;
import com.example.tinyemail.resource.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CampaignService {

    @Autowired
    private CampaignRepositoryWrapper repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    EmailService emailService;

    public CampaignResource createCampaign(CampaignResource resource) {
        String userId = resource.getUserId();
        Campaign exCampaign = repository.findCampaignByUserIdAndName(userId, resource.getName());
        if (exCampaign != null) {
            throw new AdGenericException("campaign with Name " + resource.getName() + " already exists");
        }
        resource.setUserId(userId);
        Campaign campaign = convertResourceToCampaign(resource);
        campaign = repository.save(campaign);
        CampaignResource campaignResource = convertCampaignToResource(campaign);
        return campaignResource;
    }

    public CampaignResource getCampaignById(String id) {
        Campaign campaign = repository.findCampaignById(id);
        if (campaign == null) {
            throw new AdGenericException("campaign with Id : " + id + " does not exists");
        }
        CampaignResource campaignResource = this.convertCampaignToResource(campaign);
        return campaignResource;
    }

    public List<CampaignResource> getCampaigns() {
        String userId = "userId"; // "userId" is a placeholder value
        List<Campaign> campaigns = repository.findAll(userId);
        List<CampaignResource> resources = new ArrayList<>();
        campaigns.forEach(campaign -> resources.add(convertCampaignToResource(campaign)));
        return resources;
    }

    public CampaignResource updateCampaign(CampaignResource resource) {
        String userId = resource.getUserId();
        Campaign exCampaign = repository.findCampaignById(resource.getId());
        if (exCampaign == null) {
            throw new AdGenericException("campaign with Name " + resource.getId() + " does not exists");
        }
        this.updateCampaign(resource, exCampaign);
        exCampaign = repository.save(exCampaign);
        CampaignResource campaignResource = convertCampaignToResource(exCampaign);
        return campaignResource;
    }

    public void runCampaign(CampaignRunResource resource) {
        Campaign campaign = repository.findCampaignById(resource.getCampaignId());
        if (campaign == null) {
            throw new AdGenericException("campaign with Id : " + resource.getCampaignId() + " does not exists");
        }
        if (campaign.getStatus() != CampaignStatus.PUBLISHED) {
            throw new AdGenericException("CampaignId : " + resource.getCampaignId() + " is not in published state");
        }
        this.checkRunOrPublishCondition(campaign);
        SendEmailResource sendEmailResource = new SendEmailResource();
        sendEmailResource.setTextBody("Sample Text");
        sendEmailResource.setSubject(campaign.getSubject());
        sendEmailResource.setFrom(campaign.getFrom());
        sendEmailResource.setUserId(campaign.getUserId());
        emailService.sendEmail(sendEmailResource);
    }

    private void checkRunOrPublishCondition(Campaign exCampaign) {

        if (exCampaign.getSubject() == null || exCampaign.getSubject().getSubjectLine() == null) {
            throw new AdGenericException("Subject cannot be null while campaign status is PUBLISHED");
        }
        if (exCampaign.getFrom() == null || exCampaign.getFrom().getEmailId() == null) {
            throw new AdGenericException("From cannot be null while campaign status is PUBLISHED");
        }
        if (exCampaign.getEmailTemplateId() == null) {
            throw new AdGenericException("Email Template Id cannot be null while campaign status is PUBLISHED");
        }
        if (exCampaign.getContactIds() == null || exCampaign.getContactIds().isEmpty()) {
            throw new AdGenericException(
                "At least one contact should be selected while campaign status is PUBLISHED");
        }

    }

    public CampaignResource updateCampaingStatus(String id, CampaignStatus status) {
        Campaign exCampaign = repository.findCampaignById(id);
        if (exCampaign == null) {
            throw new AdGenericException("campaign with id " + id + " does not exists");
        }
        if (CampaignStatus.PUBLISHED == status) {
            this.checkRunOrPublishCondition(exCampaign);
        }
        exCampaign.setStatus(status);
        exCampaign.setUpdated(LocalDateTime.now());
        exCampaign = repository.update(exCampaign);
        return this.convertCampaignToResource(exCampaign);
    }

    private SubjectDto updateSubject(SubjectDto reourceSubject, SubjectDto modelSub) {
        if (modelSub == null) {
            modelSub = reourceSubject;
        } else {
            if (reourceSubject.getPreviewText() != null) {
                modelSub.setPreviewText(reourceSubject.getPreviewText());
            }
            if (reourceSubject.getSubjectLine() != null) {
                modelSub.setSubjectLine(reourceSubject.getSubjectLine());
            }
        }
        return modelSub;
    }

    private FromDto updateFrom(FromDto reourceFrom, FromDto modelFrom) {
        if (modelFrom == null) {
            modelFrom = reourceFrom;
        } else {
            if (reourceFrom.getName() != null) {
                modelFrom.setName(reourceFrom.getName());
            }
            if (reourceFrom.getEmailId() != null) {
                modelFrom.setEmailId(reourceFrom.getEmailId());
            }
        }
        return modelFrom;
    }

    private void updateCampaign(CampaignResource resource, Campaign exCampaign) {
        if (resource.getName() != null) {
            exCampaign.setName(resource.getName());
        }
        if (resource.getSubject() != null) {
            exCampaign.setSubject(this.updateSubject(resource.getSubject(), exCampaign.getSubject()));
        }
        if (resource.getFrom() != null) {
            exCampaign.setFrom(this.updateFrom(resource.getFrom(), exCampaign.getFrom()));
        }
        if (resource.getEmailTemplateId() != null) {
            exCampaign.setEmailTemplateId(resource.getEmailTemplateId());
        }
        if (resource.getStatus() != null) {
            exCampaign.setStatus(resource.getStatus());
        }
        if (resource.getContactResources() != null) {
            exCampaign.setContactIds(resource.getContactResources());
        }
        if (resource.getScheduledTime() != null) {
            exCampaign.setScheduledTime(resource.getScheduledTime());
        }
    }

    private CampaignResource convertCampaignToResource(Campaign campaign) {
        CampaignResource resource = objectMapper.convertValue(campaign, CampaignResource.class);
        if (!CollectionUtils.isEmpty(campaign.getContactIds())) {
            List<String> contactResources = campaign.getContactIds();
            resource.setContactResources(contactResources);
        }
        return resource;
    }

    private Campaign convertResourceToCampaign(CampaignResource resource) {
        Campaign campaign = objectMapper.convertValue(resource, Campaign.class);
        List<String> contactResources = resource.getContactResources();
        if (contactResources != null) {
            campaign.setContactIds(contactResources);
        }
        return campaign;
    }

}

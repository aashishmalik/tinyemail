package com.example.tinyemail.resource;

import com.example.tinyemail.enums.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignResource extends BaseResource {
    private String id; // Unique identifier for the campaign
    private String name; // Name of the campaign
    private SubjectDto subject; // Subject of the email
    private String userId;
    private String emailTemplateId; // Body content of the email
    private CampaignStatus status; // e.g., "Draft", "Scheduled", "Sent", "Archived"
    private FromDto from; // Email address of the sender
    private List<String> contactResources; // List of recipient list IDs
    private LocalDateTime scheduledTime; // Time the campaign is scheduled to be sent
}

package com.example.tinyemail.model;

import com.example.tinyemail.enums.CampaignStatus;
import com.example.tinyemail.resource.FromDto;
import com.example.tinyemail.resource.SubjectDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign")
public class Campaign extends BaseModel {
    private String id; // Unique identifier for the campaign
    private String userId;
    private String name; // Name of the campaign
    private SubjectDto subject; // Subject of the email
    private String emailTemplateId; // Body content of the email
    private CampaignStatus status;
    private FromDto from; // Email address of the sender
    private List<String> contactIds; // List of recipient list IDs
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledTime; // Time the campaign is scheduled to be sent
    private boolean isDeleted;
}

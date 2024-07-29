package com.example.tinyemail.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendEmailResource {
    // if necessary add templateId and templateName
//    private String template;
    private String textBody;
    private FromDto from;
    private SubjectDto subject;
    private List<ContactResource> contactResources; // List of recipient list IDs
    private ContactResource contactResource;
    private String userId;
}

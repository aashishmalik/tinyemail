package com.example.tinyemail.resource;

import com.example.tinyemail.enums.ContactStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactResource extends BaseResource {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isSubscribed;
    private String userId;
    private Map<String, String> dynamicField;
    private ContactStatus status;
}


package com.example.tinyemail.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailTemplateResource extends BaseResource {
    private String id;
    private String name;
    private String htmlContent;
    private String cssContentContent;
    private String userId;
    private Boolean isDeleted;
    private Map<String, String> metadata;
    private boolean isPreBuilt;
    //template/layout
    private String type;
}

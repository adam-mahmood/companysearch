package com.adam.companysearch.client.feign.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class CompanyItem {
    private final String companyStatus;
    private final String addressSnippet;
    private final String dateOfCreation;

    private final String description;
    private final Map<String, String> links;
    private final String companyNumber;
    private final String title;
    private final String companyType;
    private final Address address;
    private final String kind;
    private final List<String> descriptionIdentifier;
}

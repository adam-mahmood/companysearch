package com.adam.companysearch.client.feign.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Officer {
    private final Address address;
    private final String name;
    private final String appointedOn;
    private final String resignedOn;
    private final String officerRole;
    private final Map<String, Map<String, String>> links;
    private final DateOfBirth dateOfBirth;
    private final String occupation;
    private final String countryOfResidence;
    private final String nationality;
}

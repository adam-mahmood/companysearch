package com.adam.companysearch.client.feign.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Address {
    private final String premises;
    private final String postalCode;
    private final String country;
    private final String locality;
    private final String addressLine1;
}

package com.adam.companysearch.client.feign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Officers {
    private final String etag;
    private final Map<String, String> links;
    private final String kind;
    private final int itemsPerPage;
    @JsonProperty("items")
    private final List<Officer> items;
}

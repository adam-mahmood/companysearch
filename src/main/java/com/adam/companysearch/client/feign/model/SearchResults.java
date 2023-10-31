package com.adam.companysearch.client.feign.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class SearchResults {
    private final int pageNumber;
    private final String kind;
    private final int totalResults;
    private final List<CompanyItem> items;
}

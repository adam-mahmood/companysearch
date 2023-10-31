package com.adam.companysearch.controller;

import com.adam.companysearch.api.SearchApi;
import com.adam.companysearch.model.CompanySearchRequest;
import com.adam.companysearch.model.CompanySearchResponse;
import com.adam.companysearch.service.CompanySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class CompanySearchController implements SearchApi {

    private final CompanySearchService companySearchService;

    @Override
    public ResponseEntity<CompanySearchResponse> companySearch(String xApiKey, CompanySearchRequest companySearchRequest, Boolean activeOnly) {

        CompanySearchResponse searchResponse = companySearchService.search(xApiKey, companySearchRequest, activeOnly);

        return ResponseEntity.ok(searchResponse);
    }
}

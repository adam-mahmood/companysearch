package com.adam.companysearch.service;

import com.adam.companysearch.model.CompanySearchRequest;
import com.adam.companysearch.model.CompanySearchResponse;

public interface CompanySearchService {
    CompanySearchResponse search(String xApiKey, CompanySearchRequest companySearchRequest, Boolean activeOnly);
}

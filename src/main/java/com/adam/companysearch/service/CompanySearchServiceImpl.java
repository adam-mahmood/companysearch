package com.adam.companysearch.service;

import com.adam.companysearch.client.feign.TruProxyClient;
import com.adam.companysearch.client.feign.model.CompanyItem;
import com.adam.companysearch.client.feign.model.Officer;
import com.adam.companysearch.client.feign.model.Officers;
import com.adam.companysearch.client.feign.model.SearchResults;
import com.adam.companysearch.exception.BadRequestException;
import com.adam.companysearch.exception.CompanySearchNotFoundException;
import com.adam.companysearch.mappers.CompanyMapper;
import com.adam.companysearch.model.Company;
import com.adam.companysearch.model.CompanySearchRequest;
import com.adam.companysearch.model.CompanySearchResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanySearchServiceImpl implements CompanySearchService{

    @Autowired
    private final TruProxyClient truProxyClient;

    @Autowired
    private final CompanyMapper mapper;

    @Override
    public CompanySearchResponse search(String xApiKey, CompanySearchRequest companySearchRequest, Boolean activeOnly) {

        if(StringUtils.isEmpty(companySearchRequest.getCompanyNumber()) && StringUtils.isNotEmpty(companySearchRequest.getCompanyName())){
            return searchForCompany(xApiKey, activeOnly, companySearchRequest.getCompanyName());
        }
        else if (StringUtils.isNotEmpty(companySearchRequest.getCompanyNumber())){
            return searchForCompany(xApiKey, activeOnly, companySearchRequest.getCompanyNumber());
        }

        throw new BadRequestException("You need to provide at least 'companyNumber', 'companyName', or both.");
    }

    private CompanySearchResponse searchForCompany(String xApiKey, Boolean activeOnly, String searchTerm) {
        List<CompanyItem> companyItems = Optional.ofNullable(truProxyClient.searchCompany(xApiKey, searchTerm))
                .map(SearchResults::getItems)
                .orElseThrow(() -> new CompanySearchNotFoundException("Company not found for " + searchTerm))
                .stream()
                .filter(getActive(activeOnly)).toList();

        Map<String, List<Officer>> officers = companyItems.stream().map(CompanyItem::getCompanyNumber)
                .collect(Collectors.toMap(x -> x, x -> getActiveOfficers(xApiKey,x)));
        CompanySearchResponse response = new CompanySearchResponse();
        List<Company> company = companyItems.stream().map(companyItem -> mapper.toCompany(companyItem, officers.get(companyItem.getCompanyNumber()))).toList();
        company.forEach(response::addItemsItem);
        response.totalResults(company.size());
        return response;
    }

    private List<Officer> getActiveOfficers(String xApiKey, String companyNumber) {
        return Optional.ofNullable(truProxyClient.getOfficers(xApiKey, companyNumber))
                .map(Officers::getItems)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(isActiveOfficer())
                .collect(Collectors.toList());
    }

    private Predicate<Officer> isActiveOfficer() {
        return officer -> StringUtils.isEmpty(officer.getResignedOn());
    }

    private Predicate<CompanyItem> getActive(Boolean activeOnly) {
        return companyItem -> !Objects.nonNull(activeOnly) || !activeOnly || companyItem.getCompanyStatus().equalsIgnoreCase("active");
    }
}

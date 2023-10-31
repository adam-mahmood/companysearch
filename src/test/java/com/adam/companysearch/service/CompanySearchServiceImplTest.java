package com.adam.companysearch.service;

import com.adam.companysearch.client.feign.TruProxyClient;
import com.adam.companysearch.client.feign.model.CompanyItem;
import com.adam.companysearch.client.feign.model.Officers;
import com.adam.companysearch.client.feign.model.SearchResults;
import com.adam.companysearch.exception.BadRequestException;
import com.adam.companysearch.exception.CompanySearchNotFoundException;
import com.adam.companysearch.mappers.CompanyMapper;
import com.adam.companysearch.model.CompanySearchRequest;
import com.adam.companysearch.model.CompanySearchResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanySearchServiceImplTest {

    @Mock
    private TruProxyClient truProxyClient;

    @Mock
    private CompanyMapper mapper;

    @InjectMocks
    private CompanySearchServiceImpl service;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testSearchWithoutCompanyNameAndNumber() {
        CompanySearchRequest request = new CompanySearchRequest();
        assertThrows(BadRequestException.class, () -> service.search("xApiKey", request, true));
    }

    @Test
    public void testSearchWithCompanyNameOnly() {
        String xApiKey = "xApiKey";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName("TestCompany");

        SearchResults results = SearchResults.builder().items(Collections.emptyList()).build();
       

        when(truProxyClient.searchCompany(eq(xApiKey), eq(request.getCompanyName()))).thenReturn(results);

        CompanySearchResponse response = service.search(xApiKey, request, true);
        assertEquals(0, response.getTotalResults());
    }

    @Test
    public void testSearchCompanyNotFound() {
        String xApiKey = "xApiKey";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName("TestCompany");

        when(truProxyClient.searchCompany(eq(xApiKey), eq(request.getCompanyName()))).thenReturn(null);

        assertThrows(CompanySearchNotFoundException.class, () -> service.search(xApiKey, request, true));
    }

    @Test
    public void testSearchWithBothCompanyNameAndNumber() {
        String xApiKey = "xApiKey";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName("TestCompany");
        request.setCompanyNumber("123456");

        CompanyItem companyItem =  CompanyItem.builder().companyNumber("123456").companyStatus("active").build();


        SearchResults results = SearchResults.builder().items(Collections.singletonList(companyItem)).build();

        Officers officers =  Officers.builder().items(Collections.emptyList()).build();

        when(truProxyClient.searchCompany(eq(xApiKey), eq(request.getCompanyNumber()))).thenReturn(results);
        when(truProxyClient.getOfficers(eq(xApiKey), eq(request.getCompanyNumber()))).thenReturn(officers);

        CompanySearchResponse response = service.search(xApiKey, request, true);
        assertEquals(1, response.getTotalResults());

        verify(truProxyClient, times(1)).searchCompany(eq(xApiKey), eq(request.getCompanyNumber()));
        verify(truProxyClient, times(1)).getOfficers(eq(xApiKey), eq(request.getCompanyNumber()));
        verify(truProxyClient, never()).searchCompany(eq(xApiKey), eq(request.getCompanyName()));
    }

}

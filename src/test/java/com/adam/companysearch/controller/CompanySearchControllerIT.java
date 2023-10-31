package com.adam.companysearch.controller;

import com.adam.companysearch.exception.BadRequestException;
import com.adam.companysearch.model.CompanySearchRequest;
import com.adam.companysearch.model.CompanySearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8081)  // Assuming your external service is on port 8081 during tests
public class CompanySearchControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompanySearchWithBothCompanyNumberAndName() throws Exception {
        // Given
        String xApiKey = "KjTFKEGi7R11DuvLSPaV16IJLfTPCkKV3qowOXtE";
        String companyNumber = "06500244";
        String companyName = "BBC LIMITED";
        boolean activeOnly = true;

        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);
        request.setCompanyNumber(companyNumber);

        // Stubbing the external service call using WireMock
        stubFor(get(urlPathMatching("/v1/Officers"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("CompanyNumber",equalTo(companyNumber))
                .willReturn(aResponse()
                        .withBodyFile("officers/06500244.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        stubFor(get(urlPathMatching("/v1/Search"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("Query",equalTo(companyNumber))
                .willReturn(aResponse()
                        .withBodyFile("company/06500244.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/search")
                        .header("x-api-key", xApiKey)
                        .param("activeOnly", Boolean.toString(activeOnly))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();
        CompanySearchResponse expected = objectMapper.readValue(readJsonFile("both_fields_present_expected_response.json"), CompanySearchResponse.class);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        CompanySearchResponse actualResponse = objectMapper.readValue(actualResponseBody, CompanySearchResponse.class);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expected);

        // Optionally, verify the stub was invoked
        WireMock.verify(1, getRequestedFor(urlPathMatching("/v1/Officers")));
        WireMock.verify(1, getRequestedFor(urlPathMatching("/v1/Search")));
    }

    @Test
    public void testCompanySearchWithOnlyCompanyNameAndActiveTrue() throws Exception {
        // Given
        String xApiKey = "KjTFKEGi7R11DuvLSPaV16IJLfTPCkKV3qowOXtE";
        String companyName = "BBC LIMITED";
        boolean activeOnly = true;

        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);


        // Stubbing the external service call using WireMock
        stubFor(get(urlPathMatching("/v1/Officers"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("CompanyNumber", matching("^\\d{8}$"))
                .willReturn(aResponse()
                        .withBodyFile("officers/06500244.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        stubFor(get(urlPathMatching("/v1/Search"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("Query",equalTo(companyName))
                .willReturn(aResponse()
                        .withBodyFile("company/BBC_LIMITED.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/search")
                        .header("x-api-key", xApiKey)
                        .param("activeOnly", Boolean.toString(activeOnly))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();
        CompanySearchResponse expected = objectMapper.readValue(readJsonFile("only_company_name_expected_response.json"), CompanySearchResponse.class);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        CompanySearchResponse actualResponse = objectMapper.readValue(actualResponseBody, CompanySearchResponse.class);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expected);

        // Optionally, verify the stub was invoked
        WireMock.verify(2, getRequestedFor(urlPathMatching("/v1/Officers")));
        WireMock.verify(1, getRequestedFor(urlPathMatching("/v1/Search")));
    }

    @Test
    public void testCompanySearchWithOnlyCompanyNameAndActiveNull() throws Exception {
        // Given
        String xApiKey = "KjTFKEGi7R11DuvLSPaV16IJLfTPCkKV3qowOXtE";
        String companyName = "BBC LIMITED";

        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);

        // Stubbing the external service call using WireMock
        stubFor(get(urlPathMatching("/v1/Officers"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("CompanyNumber", matching("^\\d{8}$"))
                .willReturn(aResponse()
                        .withBodyFile("officers/06500244.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        stubFor(get(urlPathMatching("/v1/Search"))
                .withHeader("x-api-key", equalTo(xApiKey))
                .withQueryParam("Query",equalTo(companyName))
                .willReturn(aResponse()
                        .withBodyFile("company/BBC_LIMITED.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/search")
                        .header("x-api-key", xApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();
        CompanySearchResponse expected = objectMapper.readValue(readJsonFile("only_company_name_expected_response_with_dissolved.json"), CompanySearchResponse.class);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        CompanySearchResponse actualResponse = objectMapper.readValue(actualResponseBody, CompanySearchResponse.class);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expected);

        // Optionally, verify the stub was invoked
        WireMock.verify(3, getRequestedFor(urlPathMatching("/v1/Officers")));
        WireMock.verify(1, getRequestedFor(urlPathMatching("/v1/Search")));
    }

    @Test
    public void testCompanySearchWithNoCompanyNameAndNumber() throws Exception {
        // Given
        String xApiKey = "KjTFKEGi7R11DuvLSPaV16IJLfTPCkKV3qowOXtE";
        boolean activeOnly = true;

        CompanySearchRequest request = new CompanySearchRequest();


        // When & Then
        Exception exception = mockMvc.perform(post("/search")
                        .header("x-api-key", xApiKey)
                        .param("activeOnly", Boolean.toString(activeOnly))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()).andReturn().getResolvedException();
        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You need to provide at least 'companyNumber', 'companyName', or both.");

    }

    public String readJsonFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources", filename)));
    }
}

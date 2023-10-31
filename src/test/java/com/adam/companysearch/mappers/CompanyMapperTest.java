package com.adam.companysearch.mappers;

import com.adam.companysearch.client.feign.model.Address;
import com.adam.companysearch.client.feign.model.CompanyItem;
import com.adam.companysearch.client.feign.model.Officer;
import com.adam.companysearch.model.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompanyMapperTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void shouldMapCompanyItemAndOfficerListToCompany() {
        // Given
        Address address = Address.builder().locality("London").country("England").postalCode("SW20 0DP").premises("5").build();
        CompanyItem companyItem = CompanyItem.builder().companyNumber("12345").companyType("Private")
                .companyType("Private").title("My Company").companyStatus("Active")
                .dateOfCreation("2021-01-01").address(address).build();

        Address address2 = Address.builder().locality("Blackpool").country("England").postalCode("BB1 0DP").premises("6").build();
        Officer officer1 =  Officer.builder().name("Charlie Dunc").officerRole("general").appointedOn("2008-02-11").address(address2).build();

        List<Officer> officerList = List.of(officer1);

        // When
        Company company = CompanyMapper.INSTANCE.toCompany(companyItem, officerList);

        // Then
        assertThat(company).isNotNull();
        assertThat(company.getCompanyNumber()).isEqualTo(companyItem.getCompanyNumber());
        assertThat(company.getCompanyType()).isEqualTo(companyItem.getCompanyType());
        assertThat(company.getTitle()).isEqualTo(companyItem.getTitle());
        assertThat(company.getCompanyStatus()).isEqualTo(companyItem.getCompanyStatus());
        // If DateOfCreation in Company is of type LocalDate, parse it to compare
        assertThat(company.getDateOfCreation()).isEqualTo(LocalDate.parse(companyItem.getDateOfCreation()));
        assertThat(company.getAddress()).usingRecursiveComparison().isEqualTo(companyItem.getAddress());
        assertThat(company.getOfficers()).usingRecursiveComparison().comparingOnlyFields("name","officerRole", "appointedOn").isEqualTo(officerList);
    }
}
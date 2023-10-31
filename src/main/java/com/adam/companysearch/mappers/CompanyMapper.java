package com.adam.companysearch.mappers;

import com.adam.companysearch.client.feign.model.CompanyItem;
import com.adam.companysearch.client.feign.model.Officer;
import com.adam.companysearch.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mappings({
            @Mapping(source = "companyItem.companyNumber", target = "companyNumber"),
            @Mapping(source = "companyItem.companyType", target = "companyType"),
            @Mapping(source = "companyItem.title", target = "title"),
            @Mapping(source = "companyItem.companyStatus", target = "companyStatus"),
            @Mapping(source = "companyItem.dateOfCreation", target = "dateOfCreation", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "companyItem.address", target = "address"),
            @Mapping(source = "officerList", target = "officers")
    })
    Company toCompany(CompanyItem companyItem, List<Officer> officerList);
}

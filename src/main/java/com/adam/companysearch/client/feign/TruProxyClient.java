package com.adam.companysearch.client.feign;

import com.adam.companysearch.client.feign.config.FeignConfig;
import com.adam.companysearch.client.feign.model.Officers;
import com.adam.companysearch.client.feign.model.SearchResults;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.name}", url = "${feign.url}", configuration = FeignConfig.class)
public interface TruProxyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/Officers")
    Officers getOfficers(@RequestHeader("x-api-key") String token, @RequestParam("CompanyNumber") String number);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/Search")
    SearchResults searchCompany(@RequestHeader("x-api-key") String token, @RequestParam("Query") String searchTerm);
}

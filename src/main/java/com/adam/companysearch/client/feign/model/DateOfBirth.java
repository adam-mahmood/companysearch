package com.adam.companysearch.client.feign.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class DateOfBirth {
    private final int month;
    private final int year;
}

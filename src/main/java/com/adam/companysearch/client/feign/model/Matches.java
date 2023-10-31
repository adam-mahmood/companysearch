package com.adam.companysearch.client.feign.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Matches {
    private final List<Integer> title;
}

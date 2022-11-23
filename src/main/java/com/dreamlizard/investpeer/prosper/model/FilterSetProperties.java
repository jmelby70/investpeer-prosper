package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "filtersets")
public class FilterSetProperties {
    private List<FilterSet> filterSetList = new ArrayList<>();

}

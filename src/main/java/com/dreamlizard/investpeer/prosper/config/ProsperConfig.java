package com.dreamlizard.investpeer.prosper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "prosper")
public class ProsperConfig
{
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String baseUrl;
}

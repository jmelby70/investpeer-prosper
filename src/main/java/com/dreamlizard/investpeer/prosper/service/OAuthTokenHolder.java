package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.model.OAuthToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class OAuthTokenHolder
{
    @Getter
    @Setter
    private OAuthToken oAuthToken;
}

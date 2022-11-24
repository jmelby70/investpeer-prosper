package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Listings;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Log
@Service
public class ListingsRestService extends ProsperRestService<Listings>
{
    public ListingsRestService(RestTemplate restTemplate, ProsperConfig prosperConfig, OAuthTokenHolder oAuthTokenHolder)
    {
        super(restTemplate, prosperConfig, oAuthTokenHolder);
    }

    public Listings getListings() throws ProsperRestServiceException
    {
        StringBuilder url = new StringBuilder(getBaseUrl());
        url.append("/listingsvc/v2/listings?limit=5000&biddable=true&invested=false");
        if (getProsperConfig().getGlobalFilters() != null)
        {
            for (Map.Entry<String, String> mapEntry : getProsperConfig().getGlobalFilters().entrySet())
            {
                url.append("&");
                url.append(mapEntry.getKey());
                url.append("=");
                url.append(mapEntry.getValue());
            }
        }
        log.info("Invoking Listings service with URL: " + url.toString());

        return getEntity(url.toString(), Listings.class);
    }
}

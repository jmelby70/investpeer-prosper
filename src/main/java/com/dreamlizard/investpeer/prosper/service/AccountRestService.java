package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Log
@Service
public class AccountRestService extends ProsperRestService
{
    public AccountRestService(RestTemplate restTemplate, ProsperConfig prosperConfig)
    {
        super(restTemplate, prosperConfig);
    }

    public Account getAccount() throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/accounts/prosper/";
        HttpEntity httpEntity = new HttpEntity(getHttpHeaders());
        ResponseEntity<Account> responseEntity = null;
        try
        {
            responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, Account.class);
        }
        catch (HttpClientErrorException.Forbidden hce)
        {

//            if (HttpStatus.FORBIDDEN.equals(responseEntity.getStatusCode()))
//            {
            log.info("Expired access token detected, re-initializing token...");
            initToken();
            httpEntity = new HttpEntity(getHttpHeaders());
            responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, Account.class);
//            }
        }

        if (responseEntity.getStatusCode().isError())
        {
            throw new ProsperRestServiceException("Error retrieving Account: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}

package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        ResponseEntity<Account> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, Account.class);
        if (HttpStatus.FORBIDDEN.equals(responseEntity.getStatusCode()))
        {
            initToken();
            responseEntity = getRestTemplate().getForEntity(url, Account.class);
        }

        if (responseEntity.getStatusCode().isError())
        {
            throw new ProsperRestServiceException("Error retrieving Account: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}

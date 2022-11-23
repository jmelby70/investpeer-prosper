package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log
@Service
public class AccountsRestService extends ProsperRestService<Account>
{
    public AccountsRestService(RestTemplate restTemplate, ProsperConfig prosperConfig)
    {
        super(restTemplate, prosperConfig);
    }

    public Account getAccount() throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/accounts/prosper/";
        return getEntity(url, Account.class);
    }

}

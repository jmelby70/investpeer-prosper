package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Log
@Service
@RequiredArgsConstructor
public class ProsperBuyNotesService
{
    private final AccountRestService accountRestService;

    public void buyNotes() throws ProsperRestServiceException
    {
        log.info("Gathering Account info...");
        Account account = accountRestService.getAccount();
        log.info("Account:\n" + account.toString());
    }
}

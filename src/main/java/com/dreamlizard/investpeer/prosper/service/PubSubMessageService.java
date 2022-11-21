package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.PubSubMessage;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;

@Log
@Service
@RequiredArgsConstructor
public class PubSubMessageService
{
    private final ProsperBuyNotesService prosperBuyNotesService;

    @Bean
    public Consumer<PubSubMessage> pubSubFunction()
    {
        return message ->
        {
            // The PubSubMessage data field arrives as a base-64 encoded string and must be decoded.
            // See: https://cloud.google.com/functions/docs/calling/pubsub#event_structure
            String decodedMessage = new String(Base64.getDecoder().decode(message.getData()), StandardCharsets.UTF_8);
            log.info("Message Received: " + decodedMessage);
            buyNotes();
        };
    }

    public void buyNotes()
    {
        try
        {
            prosperBuyNotesService.buyNotes();
        }
        catch (ProsperRestServiceException e)
        {
            log.severe("Error running Buy Notes service");
            e.printStackTrace();
        }
    }
}

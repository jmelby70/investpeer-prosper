package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.constants.AppConstants;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.PubSubMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;

@Slf4j
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
            log.info("Message Received: {}", decodedMessage);
            String runMode = decodedMessage.trim();
            prosperBuyNotesService.setRunMode(runMode);
            if (AppConstants.TEST_MODE.equalsIgnoreCase(runMode) || AppConstants.PROD_MODE.equalsIgnoreCase(runMode))
            {
                log.info("Run Mode: {}", runMode);
                buyNotes();
            }
            else
            {
                log.info("No valid run mode detected, no action taken. PubSubMessage must send 'test' or 'prod' as the run mode.");
            }
        };
    }

    public void buyNotes()
    {
        try
        {
            prosperBuyNotesService.buyNotes();
        }
        catch (ProsperRestServiceException | IOException e)
        {
            log.error("Error running Buy Notes service");
            e.printStackTrace();
        }
    }
}

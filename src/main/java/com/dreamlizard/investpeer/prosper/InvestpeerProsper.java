package com.dreamlizard.investpeer.prosper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;

@SpringBootApplication
public class InvestpeerProsper
{
    public static String RUN_MODE = null;
    public static void main(String[] args)
    {
        SpringApplication.run(InvestpeerProsper.class, args);
    }

    @Bean
    public Consumer<PubSubMessage> pubSubFunction()
    {
        return message ->
        {
            // The PubSubMessage data field arrives as a base-64 encoded string and must be decoded.
            // See: https://cloud.google.com/functions/docs/calling/pubsub#event_structure
            String decodedMessage = new String(Base64.getDecoder().decode(message.getData()), StandardCharsets.UTF_8);
            System.out.println("Run Mode Pre: " + RUN_MODE);
            System.out.println("Received Pub/Sub message with data: " + decodedMessage);
            RUN_MODE = decodedMessage;
            System.out.println("Run Mode Post: " + RUN_MODE);
        };
    }
}

package com.dreamlizard.investpeer.prosper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvestpeerProsperTest
{
    @Autowired
    private InvestpeerProsper investpeerProsper;

    @Test
    public void testFunction()
    {
        PubSubMessage pubSubMessage = new PubSubMessage();
        pubSubMessage.setData("dGVzdAo=");
        investpeerProsper.pubSubFunction().accept(pubSubMessage);
    }
}

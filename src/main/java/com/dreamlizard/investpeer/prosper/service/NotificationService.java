package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.OrdersResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService
{
    private final ProsperConfig prosperConfig;

    public void sendOrderNotification(Set<Listing> listingSet, OrdersResponse ordersResponse) throws IOException
    {
        if (ObjectUtils.isNotEmpty(prosperConfig.getSendgridApiKey()))
        {
            Email from = new Email(prosperConfig.getEmailFrom());
            String subject = "Investpeer Prosper Order Submitted";
            Email to = new Email(prosperConfig.getEmailTo());
            Content content = new Content("text/plain", getEmailBody(listingSet, ordersResponse));
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sendGrid = new SendGrid(prosperConfig.getSendgridApiKey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info("Sent email to SendGrid with response status {}", response.getStatusCode());
        }
        else
        {
            log.info("No SendGrid API key configured, skipping email notification.");
        }
    }

    private String getEmailBody(Set<Listing> listingSet, OrdersResponse ordersResponse)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Order Number: ");
        sb.append(ordersResponse.getOrder_id());
        sb.append("\nOrder Status: ");
        sb.append(ordersResponse.getOrder_status());
        sb.append("\nOrder Date: ");
        sb.append(ordersResponse.getOrder_date());
        sb.append("\nListing Count: ");
        sb.append(listingSet.size());
        sb.append("\nTotal Amount: $");
        sb.append(prosperConfig.getMinimumInvestmentAmount() * listingSet.size());
        sb.append("\n\nListings:\n");
        for (Listing listing : listingSet)
        {
            sb.append(listing.getProsper_rating());
            sb.append(" ");
            sb.append(listing.getListing_number());
            sb.append("\n");
        }

        return sb.toString();
    }
}

package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Log
@RequiredArgsConstructor
public class ProsperRestService
{
    private final RestTemplate restTemplate;
    private final ProsperConfig prosperConfig;
    private OAuthToken oAuthToken;

    protected RestTemplate getRestTemplate()
    {
        return restTemplate;
    }

    protected String getBaseUrl()
    {
        return prosperConfig.getBaseUrl();
    }

    protected HttpHeaders getHttpHeaders() throws ProsperRestServiceException
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediaTypeList);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if ((oAuthToken == null) || (ObjectUtils.isEmpty(oAuthToken.getAccess_token())))
        {
            initToken();
        }
        httpHeaders.add("Authorization", "bearer " + oAuthToken.getAccess_token());

        return httpHeaders;
    }

    protected void initToken() throws ProsperRestServiceException
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediaTypeList);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = getBaseUrl() + "/v1/security/oauth/token";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", prosperConfig.getClientId());
        map.add("client_secret", prosperConfig.getClientSecret());
        map.add("username", prosperConfig.getUsername());
        map.add("password", prosperConfig.getPassword());
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);

        log.info("Initializing OAuth Token...");
        ResponseEntity<OAuthToken> responseEntity = getRestTemplate().postForEntity(url, httpEntity, OAuthToken.class);
        if (responseEntity.getStatusCode().isError() || ObjectUtils.isEmpty(responseEntity.getBody()) || ObjectUtils.isEmpty(responseEntity.getBody().getAccess_token()))
        {
            throw new ProsperRestServiceException("Exception initializing Prosper OAuth Token" + responseEntity.getStatusCode());
        }

        oAuthToken = responseEntity.getBody();
        log.info("OAuth Token retrieved with expiry in " + oAuthToken.getExpires_in() + " seconds");

    }
}

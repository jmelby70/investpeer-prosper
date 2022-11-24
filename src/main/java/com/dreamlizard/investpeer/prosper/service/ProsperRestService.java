package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.OAuthToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//import com.dreamlizard.investpeer.prosper.model.Account;

@Log
@RequiredArgsConstructor
public class ProsperRestService<T>
{
    @Getter
    private final RestTemplate restTemplate;
    @Getter
    private final ProsperConfig prosperConfig;
    private final OAuthTokenHolder oAuthTokenHolder;

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
        if ((oAuthTokenHolder.getOAuthToken() == null) || (ObjectUtils.isEmpty(oAuthTokenHolder.getOAuthToken().getAccess_token())))
        {
            initToken();
        }
        httpHeaders.add("Authorization", "bearer " + oAuthTokenHolder.getOAuthToken().getAccess_token());

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

        oAuthTokenHolder.setOAuthToken(responseEntity.getBody());
        log.info("OAuth Token retrieved with expiry in " + oAuthTokenHolder.getOAuthToken().getExpires_in() + " seconds");

    }

    protected T getEntity(String url, Class<T> entityClass) throws ProsperRestServiceException
    {
        HttpEntity httpEntity = new HttpEntity(getHttpHeaders());
        ResponseEntity<T> responseEntity;
        try
        {
            responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, entityClass);
        }
        catch (HttpClientErrorException.Forbidden | HttpClientErrorException.Unauthorized hce)
        {

            log.info("Expired access token detected, re-initializing token...");
            initToken();
            httpEntity = new HttpEntity(getHttpHeaders());
            responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, entityClass);
        }

        if (responseEntity.getStatusCode().isError())
        {
            throw new ProsperRestServiceException("Error retrieving " + entityClass.getName() + ": " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    protected <E> T postEntity(String url, E requestEntity, Class<T> responseEntityClass) throws ProsperRestServiceException
    {
        HttpEntity<E> httpEntity = new HttpEntity<>(requestEntity, getHttpHeaders());
        ResponseEntity<T> responseEntity;
        try
        {
            responseEntity = getRestTemplate().exchange(url, HttpMethod.POST, httpEntity, responseEntityClass);
        }
        catch (HttpClientErrorException.Forbidden | HttpClientErrorException.Unauthorized hce)
        {

            log.info("Expired access token detected, re-initializing token...");
            initToken();
            httpEntity = new HttpEntity<>(requestEntity, getHttpHeaders());
            responseEntity = getRestTemplate().exchange(url, HttpMethod.POST, httpEntity, responseEntityClass);
        }

        if (responseEntity.getStatusCode().isError())
        {
            throw new ProsperRestServiceException("Error posting " + requestEntity.getClass().getName() + ": " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}

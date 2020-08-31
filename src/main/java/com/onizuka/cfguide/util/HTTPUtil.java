package com.onizuka.cfguide.util;

import com.onizuka.cfguide.dto.RestApiResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HTTPUtil {
    private final RestTemplate restTemplate;

    @Autowired
    public HTTPUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    public <T> RestApiResponse get(String url, Class<T> responseType) {
        return RestApiResponse.ofResponseEntity(restTemplate.getForEntity(url, String.class), responseType);
    }


}

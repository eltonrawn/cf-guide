package com.onizuka.cfguide.dto;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class RestApiResponse {
    private HttpStatus httpStatus;
    private Object responseBody;

    public boolean isSuccessful() {
        return (httpStatus.value() >= 200 && httpStatus.value() <= 299);
    }

    @SneakyThrows
    public static <T> RestApiResponse ofResponseEntity(ResponseEntity<String> responseEntity, Class<T> responseType) {
        T body = new Gson().fromJson(responseEntity.getBody(), responseType);
        return RestApiResponse.builder()
                .httpStatus(responseEntity.getStatusCode())
                .responseBody(body)
                .build();
    }
}

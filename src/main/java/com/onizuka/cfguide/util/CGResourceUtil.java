package com.onizuka.cfguide.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class CGResourceUtil {
    ObjectMapper objectMapper;

    public CGResourceUtil() {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public Object getResourceAsObject(String relativePath) {
        return objectMapper.readValue(getResourceAsString(relativePath),
                new TypeReference<>() {
                });
    }

    private String getResourceAsString(String relativePath) throws IOException {
        return StreamUtils.copyToString(getResource(relativePath).getInputStream(), UTF_8);
    }

    private Resource getResource(String relativePath) {
        return new ClassPathResource(relativePath);
    }
}


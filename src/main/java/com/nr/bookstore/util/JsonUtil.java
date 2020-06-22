package com.nr.bookstore.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.bookstore.exception.JsonException;
import com.nr.bookstore.log.Logger;

import java.util.Objects;

public class JsonUtil {

    private static ObjectMapper mapper;

    private static final Logger LOGGER = new Logger(JsonUtil.class);

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static <T> T fromJsonString(String jsonString, Class<T> classType) throws JsonException {
        if (Objects.isNull(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, classType);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting from JSON string: " + jsonString, e);
            throw new JsonException(e.getMessage());
        }
    }


    public static String toJsonString(Object object) throws JsonException{
        if (Objects.isNull(object)) {
            return null;
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting to JSON string", e);
            throw new JsonException(e.getMessage());
        }
    }
}

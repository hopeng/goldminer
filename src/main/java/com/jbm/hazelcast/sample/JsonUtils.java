package com.jbm.hazelcast.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Ascii;

import java.io.IOException;
import java.util.List;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error serializing object " + obj, e);
        }
    }


    public static <T> T fromJson(String json, Class<T> type) {
        Assert.notNull(json);
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException("error deserializing json: " + Ascii.truncate(json, 100, ".."), e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("error deserializing json: " + Ascii.truncate(json, 100, ".."), e);
        }
    }

    public static <T> List<T> fromJsonArray(String jsonArray, Class<T> type) {
        Assert.notNull(jsonArray);
        try {
            CollectionType valueType = MAPPER.getTypeFactory().constructCollectionType(List.class, type);
            return MAPPER.readValue(jsonArray, valueType);
        } catch (IOException e) {
            throw new RuntimeException("error deserializing json array: " + Ascii.truncate(jsonArray, 100, ".."), e);
        }

    }
}

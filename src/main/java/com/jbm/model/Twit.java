package com.jbm.model;

import com.jbm.hazelcast.sample.JsonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Database entity
 */
public class Twit implements Serializable {

    private Long id;

    private String body;

    private transient Map<String, Object> contentMap = new HashMap<>();

    public Twit(String body) {
        this.body = body;
        this.contentMap = JsonUtils.fromJson(body, Map.class);
        this.id = (Long) this.contentMap.get("id");

    }

    public Twit() {
    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    // todo work around, twitKeyStore not loading with the Twit constructor
    public <T> T get(String key) {
        if (this.contentMap == null) {
            this.contentMap = JsonUtils.fromJson(body, Map.class);
        }
        return (T) contentMap.get(key);
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

package com.jbm.hazelcast.sample;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Twit implements Serializable {

    private Long id;

    private String body;

    private transient Map<String, String> contentMap = new HashMap<>();

    public Twit(Long id, String body) {
        this.id = id;
        this.body = body;
    }

    public Twit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        this.contentMap = JsonUtils.fromJson(body, Map.class);
    }

    public Map<String, String> getContentMap() {
        return contentMap;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
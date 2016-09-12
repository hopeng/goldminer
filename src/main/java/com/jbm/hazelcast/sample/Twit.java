package com.jbm.hazelcast.sample;

import java.io.Serializable;

public class Twit implements Serializable {

    private Long id;

    private String json;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Twit(Long id, String json) {
        this.id = id;
        this.json = json;
    }

    public Twit() {
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
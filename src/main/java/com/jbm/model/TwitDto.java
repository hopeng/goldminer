package com.jbm.model;

import com.jbm.hazelcast.sample.JsonUtils;
import com.jbm.twits.TrendBias;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hopeng on 18/09/2016.
 */
public class TwitDto {

    public static final DateTimeFormatter DATETIME_PATTERN = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss Z yyyy");

    private String message;
    private ZonedDateTime createdAt;
    private TrendBias bias;

    public String getMessage() {
        return message;
    }

    public TwitDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public TwitDto setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TwitDto setCreatedAt(String createdAt) {
        this.createdAt = ZonedDateTime.parse(createdAt, DATETIME_PATTERN);
        return this;
    }

    public TrendBias getBias() {
        return bias;
    }

    public TwitDto setBias(TrendBias bias) {
        this.bias = bias;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

package com.jbm.twits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hopeng on 11/09/2016.
 */
public class CurrencyTwitListener implements TwitListener {
    private static final Logger log = LoggerFactory.getLogger(CurrencyTwitListener.class);

    public enum Bias {Long, Short, Neutral}

    private static List<String> LONG_HINTS = Arrays.asList("buy", "bought", "long", "bullish", "strong", "up");
    private static List<String> SHORT_HINTS = Arrays.asList("sell", "sold", "short", "bearish", "weak", "down");

//    todo persisted collection, hazelcast?
    List<String> longTwits;
    List<String> shortTwits;

    public void onTwit(Map<String, Object> message) {
        String text = (String) message.get("text");
        String createdAt = (String) message.get("created_at");
        Bias bias = analyseBias(text);

        log.info("{} - {}: {}", bias, createdAt, text.replaceAll("\n", "\t"));

        // todo send to websocket

    }

    // todo use LingPipe for NLP
    protected Bias analyseBias(String text) {
        List<String> wordList = Arrays.asList(text.toLowerCase().split("\\b+"));

        long longScore = LONG_HINTS.stream()
                .filter(wordList::contains)
                .count();

        long shortScore = SHORT_HINTS.stream()
                .filter(wordList::contains)
                .count();

        return longScore > shortScore
                ? Bias.Long
                : longScore == shortScore ? Bias.Neutral : Bias.Short;
    }
}

package com.jbm.twits;

import com.jbm.model.Twit;
import com.jbm.model.TwitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hopeng on 18/09/2016.
 */
public class TwitDtoCreator {
    private static final Logger log = LoggerFactory.getLogger(TwitDtoCreator.class);

    private static List<String> LONG_HINTS = Arrays.asList("buy", "bought", "long", "bullish", "strong", "up", "rise");
    private static List<String> SHORT_HINTS = Arrays.asList("sell", "sold", "short", "bearish", "weak", "down", "fall");

    public TwitDto createDto(Twit twit) {
        String text = twit.get("text");
        String createdAt = twit.get("created_at");
        TrendBias bias = analyseBias(text);

        log.info("{} - {}: {}", bias, createdAt, text.replaceAll("\n", "\t"));

        TwitDto result = new TwitDto()
                .setMessage(text)
                .setCreatedAt(createdAt)
                .setBias(bias);

        return result;
    }

    // todo use LingPipe for NLP
    protected TrendBias analyseBias(String text) {
        List<String> wordList = Arrays.asList(text.toLowerCase().split("\\b+"));

        long longScore = LONG_HINTS.stream()
                .filter(wordList::contains)
                .count();

        long shortScore = SHORT_HINTS.stream()
                .filter(wordList::contains)
                .count();

        return longScore > shortScore
                ? TrendBias.Long
                : longScore == shortScore ? TrendBias.Neutral : TrendBias.Short;
    }
}

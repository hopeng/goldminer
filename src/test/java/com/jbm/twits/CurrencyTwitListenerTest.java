package com.jbm.twits;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.jbm.model.Twit;
import com.jbm.model.TwitDto;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Created by hopeng on 17/09/2016.
 */
public class CurrencyTwitListenerTest {

    @Test
    public void testDateParse() {
        DateTimeFormatter formatter = TwitDto.DATETIME_PATTERN;
        ZonedDateTime dt = ZonedDateTime.parse("Fri Sep 09 11:40:51 +0000 2016", formatter);
        assertEquals(dt.format(formatter), "Fri Sep 09 11:40:51 +0000 2016");
    }

    @Test
    public void testOnTwit() throws IOException {
        String json = Resources.toString(Resources.getResource("t1.json"), Charsets.UTF_8);

        CurrencyTwitListener listener = new CurrencyTwitListener();
        listener.onTwit(new Twit(json));
    }
}

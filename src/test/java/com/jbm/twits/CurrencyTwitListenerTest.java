package com.jbm.twits;

import org.junit.Test;

import static com.jbm.twits.CurrencyTwitListener.Bias.Long;
import static com.jbm.twits.CurrencyTwitListener.Bias.Neutral;
import static com.jbm.twits.CurrencyTwitListener.Bias.Short;
import static org.junit.Assert.assertEquals;

/**
 * Created by hopeng on 17/09/2016.
 */
public class CurrencyTwitListenerTest {

    @Test
    public void testBias() {
        CurrencyTwitListener listener = new CurrencyTwitListener();

        assertEquals(Long, listener.analyseBias("this is going up!"));
        assertEquals(Short, listener.analyseBias("it's a bearish day today!"));
        assertEquals(Neutral, listener.analyseBias("flat really"));
        assertEquals(Neutral, listener.analyseBias("no update today"));
        assertEquals(Neutral, listener.analyseBias("download my magic strategy"));

    }

}

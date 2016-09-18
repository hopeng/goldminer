package com.jbm.twits;

import org.junit.Test;

import static com.jbm.twits.TrendBias.Long;
import static com.jbm.twits.TrendBias.Neutral;
import static com.jbm.twits.TrendBias.Short;
import static org.junit.Assert.assertEquals;

/**
 * Created by hopeng on 18/09/2016.
 */
public class TwitDtoCreatorTest {


    @Test
    public void testBias() {
        TwitDtoCreator creator = new TwitDtoCreator();

        assertEquals(Long, creator.analyseBias("this is going up!"));
        assertEquals(Short, creator.analyseBias("it's a bearish day today!"));
        assertEquals(Neutral, creator.analyseBias("flat really"));
        assertEquals(Neutral, creator.analyseBias("no update today"));
        assertEquals(Neutral, creator.analyseBias("download my magic strategy"));
    }

}
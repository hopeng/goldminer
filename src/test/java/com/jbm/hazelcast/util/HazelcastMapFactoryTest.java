package com.jbm.hazelcast.util;

import com.jbm.model.Twit;
import org.junit.Test;

import java.util.Map;

/**
 * Created by hopeng on 15/09/2016.
 */
public class HazelcastMapFactoryTest {

    @Test
    public void getMap() throws Exception {
        Map<Long, Twit> testMap1 = HazelcastMapFactory.getMap("testMap1");
        testMap1.put(100L, new Twit(100L, "hello man lala"));
        testMap1.put(400L, new Twit(400L, "hello man's lala"));

        Map<Long, Twit> testMap2 = HazelcastMapFactory.getMap("testMap2");
        testMap2.put(200L, new Twit(200L, "hello man"));
    }

}
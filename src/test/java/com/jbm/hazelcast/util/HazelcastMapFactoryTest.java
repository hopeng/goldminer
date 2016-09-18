package com.jbm.hazelcast.util;

import com.jbm.model.Twit;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by hopeng on 15/09/2016.
 */
public class HazelcastMapFactoryTest {

    @Test
    public void getMap() throws Exception {
        Map<Long, Twit> testMap1 = HazelcastMapFactory.getMap("testMap1");
        testMap1.clear();
        Twit t1 = new Twit("{\"id\": 774210958499512322, \"text\":\"hello man lala\"}");
        testMap1.put(t1.getId(), t1);

        // load again, expect transient fields populated
        Map<Long, Twit> testMap1Reloaded = HazelcastMapFactory.getMap("testMap1");
        assertEquals("hello man lala", testMap1Reloaded.get(774210958499512322L).get("text"));

        t1 = new Twit("{\"id\": 774210958499512322, \"text\":\"hello man's lala\"}");
        testMap1.put(t1.getId(), t1);

        Map<Long, Twit> testMap2 = HazelcastMapFactory.getMap("testMap2");
        testMap1.clear();
        Twit t2 = new Twit("{\"id\": 974210958499512322, \"text\":\"hello man\"}");
        testMap2.put(t2.getId(),t2);
    }
}

package com.jbm.hazelcast.sample;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jbm.hazelcast.TwitMapStore;
import com.jbm.model.Twit;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hopeng on 11/09/2016.
 */
public class LoadTwit {

    public static void main(String[] args) throws Exception {
        String mapName = "twitTest";

        HazelcastInstance node = Hazelcast.getOrCreateHazelcastInstance(createNewConfig(mapName));
        IMap<Long, Twit> twitsMap = node.getMap(mapName);
        twitsMap.loadAll(true);
        addElement(twitsMap, Resources.toString(Resources.getResource("t1.json"), Charsets.UTF_8));
        addElement(twitsMap, Resources.toString(Resources.getResource("t2.json"), Charsets.UTF_8));

        twitsMap.evictAll();
        twitsMap.loadAll(true);
        System.out.printf("# Map store has %d elements\n", twitsMap.size());

        Twit t1 = twitsMap.get(774210958499512322L);
        System.out.printf("t1: %s\n", t1);

        node.shutdown();
    }

    private static void addElement(IMap<Long, Twit> twitsMap, String twitJson) throws IOException {
        Map<String, Object> t1 = JsonUtils.fromJson(twitJson, Map.class);
        Long id = (Long) t1.get("id");
        twitsMap.put(id, new Twit(id, twitJson));
    }

    private static Config createNewConfig(String mapName) {
        Config config = new XmlConfigBuilder().build();
        config.setInstanceName("goldminerHazelcast")
                .getMapConfig(mapName)
                .setMapStoreConfig(new MapStoreConfig()
                        .setImplementation(new TwitMapStore(mapName))
                        .setWriteDelaySeconds(0));
        return config;
    }
}
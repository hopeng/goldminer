package com.jbm.hazelcast.util;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.jbm.hazelcast.TwitMapStore;

import java.util.Map;

/**
 * Created by hopeng on 11/09/2016.
 */
public class HazelcastMapFactory {


    private static String INSTANCE_NAME = "goldminerHazelcast";

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("shutting down hazelcast");
                Hazelcast.shutdownAll();
            }
        });
    }

    public static <K,V> Map<K, V> getMap(String mapName) {
        return Hazelcast.getOrCreateHazelcastInstance(createNewConfig(mapName)).getMap(mapName);
    }


    private static Config createNewConfig(String mapName) {
        Config config = new XmlConfigBuilder().build();
        config.setInstanceName(INSTANCE_NAME)
                .getMapConfig(mapName)
                .setMapStoreConfig(new MapStoreConfig()
                        .setImplementation(new TwitMapStore(mapName))
                        .setWriteDelaySeconds(0));
        return config;
    }
}

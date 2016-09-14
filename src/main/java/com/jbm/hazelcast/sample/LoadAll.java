package com.jbm.hazelcast.sample;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStore;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by hopeng on 11/09/2016.
 */
public class LoadAll {

    public static void main(String[] args) throws Exception {
        String mapName = "twits";

        HazelcastInstance node = Hazelcast.getOrCreateHazelcastInstance(createNewConfig(mapName));
        IMap<Long, Twit> twitsMap = node.getMap(mapName);
        twitsMap.loadAll(true);
        addElement(twitsMap, Resources.toString(Resources.getResource("t1.json"), Charsets.UTF_8));
        addElement(twitsMap, Resources.toString(Resources.getResource("t2.json"), Charsets.UTF_8));

        twitsMap.evictAll();
        twitsMap.loadAll(true);
        System.out.printf("# Map store has %d elements\n", twitsMap.size());

        node.shutdown();
    }

    private static void addElement(IMap<Long, Twit> twitsMap, String twitJson) throws IOException {
        Map<String, Object> t1 = JsonUtils.fromJson(twitJson, Map.class);
        Long id = (Long) t1.get("id");
        twitsMap.put(id, new Twit(id, twitJson));
    }

    private static void populateMap(IMap<Integer, Integer> map, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            map.put(i, i);
        }
    }

    private static Config createNewConfig(String mapName) {
        TwitMapStore mapStore = new TwitMapStore();

        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(mapStore);
        mapStoreConfig.setWriteDelaySeconds(0);

        XmlConfigBuilder configBuilder = new XmlConfigBuilder();
        Config config = configBuilder.build();
        config.setInstanceName("goldminerHazelcast");
        MapConfig mapConfig = config.getMapConfig(mapName);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        return config;
    }

    private static class SimpleStore implements MapStore<Integer, Integer> {

        private ConcurrentMap<Integer, Integer> store = new ConcurrentHashMap<Integer, Integer>();

        @Override
        public void store(Integer key, Integer value) {
            store.put(key, value);
        }

        @Override
        public void storeAll(Map<Integer, Integer> map) {
            Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
            for (Map.Entry<Integer, Integer> entry : entrySet) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                store(key, value);
            }
        }

        @Override
        public void delete(Integer key) {
        }

        @Override
        public void deleteAll(Collection<Integer> keys) {
        }

        @Override
        public Integer load(Integer key) {
            return store.get(key);
        }

        @Override
        public Map<Integer, Integer> loadAll(Collection<Integer> keys) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (Integer key : keys) {
                Integer value = load(key);
                map.put(key, value);
            }
            return map;
        }

        @Override
        public Set<Integer> loadAllKeys() {
            return store.keySet();
        }
    }
}
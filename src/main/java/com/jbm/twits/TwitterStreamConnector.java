package com.jbm.twits;

import com.jbm.hazelcast.util.HazelcastMapFactory;
import com.jbm.model.Twit;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStreamConnector {

    private static final Logger log = LoggerFactory.getLogger(TwitterStreamConnector.class);

    /**
     * Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream
     */
    final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);
    final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);

    List<TwitListener> twitListenerList = new CopyOnWriteArrayList<>();

    StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

    Map<Long, Twit> storageMap;

    Thread msgConsumer;
    private Client hosebirdClient;
    private String mapName = "TWITS_";

    public TwitterStreamConnector() {
        hosebirdEndpoint
//                .followings(Arrays.asList(1234L, 566788L))
                .languages(Arrays.asList("en"));
    }

    // todo make it re-runnable
    public TwitterStreamConnector setContentFilters(List<String> contentFilters) {
        hosebirdEndpoint.trackTerms(contentFilters);
        mapName += contentFilters.get(0);
        storageMap = HazelcastMapFactory.getMap(mapName);
        return this;
    }

    public TwitterStreamConnector addListener(TwitListener listener) {
        twitListenerList.add(listener);
        return this;
    }

    public void disconnect() {
        hosebirdClient.stop();
    }

    public synchronized TwitterStreamConnector connect() {
        if (hosebirdClient != null) {
            throw new RuntimeException("already connected");
        }

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);

// These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(
                "eFn9q6cGiRR6mzOYiNFWe4qEp",
                "5t6FpUCcOO8puNGmAKEZOgh6sG515M9F8hnmt4y5a50RzDpZpl",
                "331953143-HkWN2o1u6ah2lHMTY68bwArqepQNqb3XnxIJHOWL",
                "lNM0IFXXeR7NJCgEgLZw98WQh4nGDLSBB111ozC1ov9uu");

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        hosebirdClient = builder.build();
        hosebirdClient.connect();

        msgConsumer = new Thread() {
            @Override
            public void run() {
                while (!hosebirdClient.isDone()) {

                    try {
                        String msg = msgQueue.take();
//                        log.info("going to save twit to storage {}", mapName);
                        Twit twit = new Twit(msg);
                        storageMap.put(twit.getId(), twit);

                        twitListenerList.forEach(listener -> listener.onTwit(twit));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        msgConsumer.start();
        return this;
    }

    public void waitForDone(long timeout) {
        if (msgConsumer != null) {
            try {
                msgConsumer.join(timeout);
            } catch (InterruptedException ignored) {
            }
        }
    }

}

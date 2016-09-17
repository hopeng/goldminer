package com.jbm;

import com.jbm.twits.CurrencyTwitListener;
import com.jbm.twits.TwitterStreamConnector;
import com.jbm.ws.JettyWebsocketServer;

import static java.util.Arrays.asList;

public class GoldMinerMain {

    public static void main(String args[]) {
        JettyWebsocketServer.getInstance()
                .setPort(9090)
                .start();


        TwitterStreamConnector gbpConnector = new TwitterStreamConnector()
                .setContentFilters(asList("GBPUSD"))
                .addListener(new CurrencyTwitListener())
                .connect();

//        com.jbm.twits.TwitterStreamConnector audConnector = new com.jbm.twits.TwitterStreamConnector()
//                .setContentFilters(asList("AUDUSD"))
//                .addListener(new com.jbm.twits.CurrencyTwitListener())
//                .connect();

//        com.jbm.twits.TwitterStreamConnector eurConnector = new com.jbm.twits.TwitterStreamConnector()
//                .setContentFilters(asList("EURUSD"))
//                .addListener(new com.jbm.twits.CurrencyTwitListener())
//                .connect();

        gbpConnector.waitForDone(Long.MAX_VALUE);
//        audConnector.waitForDone(Long.MAX_VALUE);
//        eurConnector.waitForDone(Long.MAX_VALUE);
    }
}

package com.jbm.ws;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * get price from http://webrates.truefx.com/rates/connect.html?f=csv
 * parse the CSV and send to frontend through websocket session
 * todo
 */
public class PriceQuoteRetriever {
    private WsClientSessionManager wsClientSessionManager = WsClientSessionManager.getInstance();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public PriceQuoteRetriever() {
//        executorService.schedule()

    }
}

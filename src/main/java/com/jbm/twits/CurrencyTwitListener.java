package com.jbm.twits;

import com.jbm.model.EventType;
import com.jbm.model.Twit;
import com.jbm.model.TwitDto;
import com.jbm.ws.WsClientSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hopeng on 11/09/2016.
 */
public class CurrencyTwitListener implements TwitListener {
    private static final Logger log = LoggerFactory.getLogger(CurrencyTwitListener.class);
    private TwitDtoCreator twitDtoCreator = new TwitDtoCreator();

    private WsClientSessionManager wsClientSessionManager = WsClientSessionManager.getInstance();

    public void onTwit(Twit twit) {
        TwitDto dto = twitDtoCreator.createDto(twit);
        wsClientSessionManager.broadcast(EventType.TradeBias, dto);
    }

}

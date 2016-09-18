package com.jbm.ws;

import com.jbm.hazelcast.util.HazelcastMapFactory;
import com.jbm.model.Twit;
import com.jbm.model.TwitDto;
import com.jbm.twits.TwitDtoCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/events/")
public class ServerEventSocket {
    private static final Logger log = LoggerFactory.getLogger(ServerEventSocket.class);
    private WsClientSessionManager wsClientSessionManager = WsClientSessionManager.getInstance();
    private TwitDtoCreator twitDtoCreator = new TwitDtoCreator();

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        wsClientSessionManager.addSession(sess);
        System.out.println("Socket Connected: " + sess);
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        System.out.println("Received TEXT message: " + message);
        if ("reload".equals(message)) {
//            todo better way to retrieve map, avoid hardcoding map name
//            todo async and avoid duplicate action
            Map<String, Twit> twitsMap = HazelcastMapFactory.getMap("TWITS_GBPUSD");
            List<Twit> twits = new ArrayList<>(twitsMap.values());
            Collections.sort(twits, (o1, o2) -> o1.getId().compareTo(o2.getId())); // todo sort during getMap()?
            twits.forEach(twit -> {
                try {
                    TwitDto dto = twitDtoCreator.createDto(twit);
                    session.getBasicRemote().sendText(String.valueOf(dto));
                } catch (IOException e) {
                    log.error("failed to send twit to ws client", e);
                }
            });
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason, Session session) {
        wsClientSessionManager.removeSession(session.getId());
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        log.error("websocket error", cause);
    }

}

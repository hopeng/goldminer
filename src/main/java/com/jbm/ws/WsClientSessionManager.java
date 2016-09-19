package com.jbm.ws;

import com.jbm.model.EventType;
import com.jbm.model.EventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by hopeng on 18/09/2016.
 */
public class WsClientSessionManager {
    private static final Logger log = LoggerFactory.getLogger(WsClientSessionManager.class);
    private static final WsClientSessionManager instance = new WsClientSessionManager();

    private Map<String, Session> sessions = new WeakHashMap<>();

    public static WsClientSessionManager getInstance() {
        return instance;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);

    }

    public void addSession(Session session) {
        sessions.put(session.getId(), session);
    }

    public void broadcast(EventType type, Object message) {
        for (Session session : sessions.values()) {
            try {
                sendEvent(session, type, message);
            } catch (Exception e) {
                log.error("failed to broadcast message", e);
            }
        }
    }

    public void sendEvent(Session session, EventType eventType, Object payload) throws IOException {
        EventWrapper event = new EventWrapper(eventType, payload);
        session.getBasicRemote().sendText(String.valueOf(event));
    }
}

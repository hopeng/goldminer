package com.jbm.ws;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.server.ServerContainer;

/**
 * Created by hopeng on 17/09/2016.
 */
public class JettyWebsocketServer {
    private static Logger log = LoggerFactory.getLogger(JettyWebsocketServer.class);

    private static JettyWebsocketServer instance;
    private int port = 8080;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutting down " + JettyWebsocketServer.class.getSimpleName());
            getInstance().stop();
        }));
    }

    public static synchronized JettyWebsocketServer getInstance() {
        if (instance == null) {
            instance = new JettyWebsocketServer();
        }
        return instance;
    }

    private Server server;

    public JettyWebsocketServer setPort(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(ServerEventSocket.class);

            server.start();
            server.dump(System.err);
//            server.join();
        } catch (Throwable t) {
            log.error("cannot start websocket server", t);
        }
    }

    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                log.error("cannot stop websocket server", e);
            }
        }
    }
}

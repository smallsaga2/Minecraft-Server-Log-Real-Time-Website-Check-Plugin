package com.example.webconsole;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/console")
public class WebSocketServer {

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    private Server server;

    public void start() {
        try {
            server = new Server(8081); // WebSocket 서버 포트를 8081로 설정

            // ServletContextHandler 생성
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            // WebSocket 서버 컨테이너 초기화
            WebSocketServerContainerInitializer.configureContext(context).addEndpoint(WebSocketServer.class);

            // WebSocket 서버 시작
            server.start();
            logger.info("WebSocket server started on port 8081");

            // WebSocket 서버를 블록 없이 비동기적으로 실행
            new Thread(() -> {
                try {
                    server.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 스레드 인터럽트 상태를 다시 설정
                    logger.log(Level.SEVERE, "WebSocket server interrupted", e);
                }
            }).start();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start WebSocket server", e);
        }
    }

    public void stop() {
        try {
            if (server != null) {
                server.stop();
                logger.info("WebSocket server stopped.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to stop WebSocket server", e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("New WebSocket connection established: " + session.getId());
        try {
            session.getBasicRemote().sendText("Connected to WebSocket server");
        } catch (IOException e) {
            logger.severe("Failed to send message to session " + session.getId() + ": " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        broadcast(message);
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Received message: " + message + " from session: " + session.getId());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        logger.info("WebSocket connection closed: " + session.getId());
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.severe("Failed to broadcast message: " + e.getMessage());
            }
        }
    }
}

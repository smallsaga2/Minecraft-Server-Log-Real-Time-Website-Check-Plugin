package com.example.webconsole;

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

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        if (logger.isLoggable(Level.INFO)) {
            logger.info("New WebSocket connection established: " + session.getId());
        }
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
        if (logger.isLoggable(Level.INFO)) {
            logger.info("WebSocket connection closed: " + session.getId());
        }
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

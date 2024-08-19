package com.example.webconsole;

import com.sun.net.httpserver.HttpServer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/console")
public class WebSocketServer {

    private final int port;
    private HttpServer server;
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", httpExchange -> {
                String response = "WebSocket server is running";
                httpExchange.sendResponseHeaders(200, response.length());
                httpExchange.getResponseBody().write(response.getBytes());
                httpExchange.close();
            });
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

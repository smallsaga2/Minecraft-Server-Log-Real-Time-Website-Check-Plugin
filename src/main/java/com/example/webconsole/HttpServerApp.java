package com.example.webconsole;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServerApp {

    private final int port;
    private HttpServer server;
    private static final Logger logger = Logger.getLogger(HttpServerApp.class.getName());

    public HttpServerApp(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String requestURI = exchange.getRequestURI().getPath();
                    if ("/".equals(requestURI) || "/index.html".equals(requestURI)) {
                        InputStream is = getClass().getResourceAsStream("/index.html");
                        if (is == null) {
                            exchange.sendResponseHeaders(404, -1);
                            return;
                        }
                        byte[] response = is.readAllBytes();
                        exchange.sendResponseHeaders(200, response.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response);
                        }
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                }
            });

            server.start();
            if (logger.isLoggable(Level.INFO)) {
                logger.info("HTTP server started on port " + port);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start HTTP server", e);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("HTTP server stopped.");
        }
    }
}

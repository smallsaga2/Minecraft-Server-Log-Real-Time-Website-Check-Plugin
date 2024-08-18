package com.example.webconsole;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebConsolePlugin extends JavaPlugin {
    private HttpServer server;

    @Override
    public void onEnable() {
        getLogger().info("WebConsolePlugin has been enabled!");
        startWebServer();
    }

    @Override
    public void onDisable() {
        getLogger().info("WebConsolePlugin has been disabled!");
        stopWebServer();
    }

    private void startWebServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", exchange -> {
                String response = "<html><body>Welcome to the Web Console</body></html>";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });
            server.start();
            getLogger().info("Web server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopWebServer() {
        if (server != null) {
            server.stop(0);
            getLogger().info("Web server stopped");
        }
    }
}

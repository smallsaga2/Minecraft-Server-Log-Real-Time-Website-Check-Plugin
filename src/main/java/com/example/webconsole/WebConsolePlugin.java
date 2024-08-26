package com.example.webconsole;

import org.bukkit.plugin.java.JavaPlugin;

public class WebConsolePlugin extends JavaPlugin {

    private HttpServerApp httpServer;
    private WebSocketServer webSocketServer;

    @Override
    public void onEnable() {
        getLogger().info("WebConsole Plugin enabled!");

        // HTTP 서버 시작
        httpServer = new HttpServerApp(8080); // HTTP 서버 포트 8080
        httpServer.start();

        // WebSocket 서버 시작
        webSocketServer = new WebSocketServer(); // WebSocket 서버 포트 8081
        webSocketServer.start();
    }

    @Override
    public void onDisable() {
        getLogger().info("WebConsole Plugin disabled!");

        // HTTP 서버 종료
        if (httpServer != null) {
            httpServer.stop();
        }

        // WebSocket 서버 종료
        if (webSocketServer != null) {
            webSocketServer.stop();
        }
    }
}

package com.example.webconsole;

import org.bukkit.plugin.java.JavaPlugin;

public class WebConsolePlugin extends JavaPlugin {

    private WebSocketServer webSocketServer;

    @Override
    public void onEnable() {
        getLogger().info("WebConsole Plugin enabled!");

        // WebSocket 서버 시작
        webSocketServer = new WebSocketServer(8080); // 포트 번호 8080으로 설정
        webSocketServer.start();
    }

    @Override
    public void onDisable() {
        getLogger().info("WebConsole Plugin disabled!");

        // WebSocket 서버 종료
        if (webSocketServer != null) {
            webSocketServer.stop();
        }
    }
}

package com.example.webconsole;

import org.bukkit.plugin.java.JavaPlugin;

public class WebConsolePlugin extends JavaPlugin {

    private HttpServerApp httpServer;

    @Override
    public void onEnable() {
        getLogger().info("WebConsole Plugin enabled!");

        // HTTP 서버 시작
        httpServer = new HttpServerApp(8080); // HTTP 서버 포트 8080
        httpServer.start();

        // WebSocket 서버는 @ServerEndpoint에 의해 자동으로 시작됨
    }

    @Override
    public void onDisable() {
        getLogger().info("WebConsole Plugin disabled!");

        // HTTP 서버 종료
        if (httpServer != null) {
            httpServer.stop();
        }

        // WebSocket 서버는 별도로 종료 메소드가 필요할 수 있음 (보통 자동 관리됨)
    }
}

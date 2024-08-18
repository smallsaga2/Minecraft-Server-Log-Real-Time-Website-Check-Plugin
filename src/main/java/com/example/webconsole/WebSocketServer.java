package com.example.webconsole;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import java.io.IOException;

@ServerEndpoint("/ws")
public class WebSocketServer {

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // 메시지 처리 로직 (예: 서버 명령어 실행)
        session.getBasicRemote().sendText("Received: " + message);
    }
}

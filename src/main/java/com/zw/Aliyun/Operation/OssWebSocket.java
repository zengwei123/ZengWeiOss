package com.zw.Aliyun.Operation;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/websocket")
@Component
public class OssWebSocket {
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private static Session session;
    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session=session;
        sendMessage("连接建立成功");
    }

    /**
     */
    @OnClose
    public void onClose(){
        System.out.println("连接断开");
    }

    /**
     * 收到客户端消息后调用
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        session.close();
    }

    /**
     * 发送信息。
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String message) throws IOException {
        if(session!=null){
            session.getBasicRemote().sendText(message);
        }
    }
}

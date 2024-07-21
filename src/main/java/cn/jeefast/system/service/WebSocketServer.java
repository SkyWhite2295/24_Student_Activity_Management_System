package cn.jeefast.system.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cn.jeefast.system.entity.Message;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;


@ServerEndpoint("/webSocket/{username}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数
    private static AtomicInteger onlineNum = new AtomicInteger();

    //用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if(session != null){
            synchronized (session) {
                //System.out.println("发送数据：" + message);
                session.getBasicRemote().sendText(message);
            }
        }
    }
    //    //给指定用户发送信息
//    public void sendInfo(String userName, String message){
//        Session session = sessionPools.get(userName);
//        try {
//            sendMessage(session, message);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    // 群发消息
    public void broadcast(String userName,String message){
        for (Session session: sessionPools.values()) {
            try {
                if(session==sessionPools.get(userName)) {
                    continue;
                }
                //System.out.println(session);
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String userName){
        sessionPools.put(userName, session);
        addOnlineCount();
    }

    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "username") String userName){
        sessionPools.remove(userName);
        subOnlineCount();
    }

    //收到客户端信息后，根据接收人的username把消息推下去或者群发
    // to=-1群发消息
    @OnMessage
    public void onMessage(String message) throws IOException{
        System.out.println("server get" + message);
        Message msg=JSON.parseObject(message, Message.class);
        msg.setDate(new Date());


        broadcast(msg.from,JSON.toJSONString(msg,true));

    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
//        System.out.println("发生错误");
////        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

    public static AtomicInteger getOnlineNumber() {
        return onlineNum;
    }

    public static ConcurrentHashMap<String, Session> getSessionPools() {
        return sessionPools;
    }
}

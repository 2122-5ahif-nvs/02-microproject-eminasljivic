package at.htl.busmanagement.socket;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint("/bus-updates/{username}")
public class InfoSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();
    Map<String, Session> registeredSessions = new ConcurrentHashMap<>();
    Thread busThread;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        if(busThread == null){
            busThread = new Thread(() -> {
                try {
                    while(true){
                        Thread.sleep(10000);
                        broadcastToRegistered("Bus 1 arrived at the next stop: " + LocalDateTime.now());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            busThread.start();
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        registeredSessions.put(username, sessions.get(username));
        if (!message.equalsIgnoreCase("_ready_")) {
            sessions.get(username).getAsyncRemote().sendObject(
                String.format("%s registered for updates concerning bus %s", username, message),
                result -> {
                    if (result.getException() != null) {
                        System.out.println("Unable to send message: " + result.getException());
                    }
                }
            );
        }
    }

    private void broadcastToRegistered(String message) {
        registeredSessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}

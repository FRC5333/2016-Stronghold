package frc.team5333.webui.websockets;

import frc.team5333.core.Core;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.script.ScriptException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class SocketConfig {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        if (message.equalsIgnoreCase("initial")) {
            try {
                session.getRemote().sendString(Core.config.toJSON());
            } catch (IOException | ScriptException e) {}
        } else {
            FileWriter writer = new FileWriter(Core.config.parent_file);
            writer.write(message);
            writer.close();
            Core.config.reload();

            try {
                session.getRemote().sendString(Core.config.toJSON());
            } catch (IOException | ScriptException e) {}
        }
    }
}

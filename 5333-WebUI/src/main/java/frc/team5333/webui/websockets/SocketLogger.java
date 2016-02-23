package frc.team5333.webui.websockets;

import jaci.openrio.toast.core.command.CommandBus;
import jaci.openrio.toast.lib.log.LogHandler;
import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.log.SysLogProxy;
import jaci.openrio.toast.lib.util.Pretty;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class SocketLogger implements LogHandler {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        try {
            BufferedReader file_reader = new BufferedReader(new FileReader(SysLogProxy.recentOut));
            String ln;
            session.getRemote().sendString("***** BEGIN BACKLOG *****");
            while ((ln = file_reader.readLine()) != null)
                session.getRemote().sendString(ln);
            session.getRemote().sendString("***** END BACKLOG *****");
        } catch (IOException e) { }
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        CommandBus.parseMessage(message);
    }

    @Override
    public void onLog(String level, String message, String formatted, Logger logger) {
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(Pretty.strip(formatted));
            } catch (Exception e) { }
        });
    }
}

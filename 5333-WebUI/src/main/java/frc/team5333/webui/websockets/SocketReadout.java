package frc.team5333.webui.websockets;

import com.grack.nanojson.JsonObject;
import edu.wpi.first.wpilibj.CANTalon;
import frc.team5333.core.hardware.IO;
import frc.team5333.webui.WebHandler;
import jaci.openrio.toast.core.shared.GlobalBlackboard;
import jaci.openrio.toast.lib.state.RobotState;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class SocketReadout {

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
    public void message(Session session, String message) throws IOException { }

    public static void tick(RobotState state) {
        JsonObject obj = new JsonObject();
        obj.put("shooter_top_throttle", IO.motor_flywheel_top.get() * 100);
        obj.put("shooter_btm_throttle", IO.motor_flywheel_bottom.get() * 100);
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(WebHandler.jsonToString(obj));
            } catch (IOException e) {}
        });
    }
}

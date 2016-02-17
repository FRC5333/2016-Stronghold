package frc.team5333.webui.websockets;

import com.grack.nanojson.JsonObject;
import edu.wpi.first.wpilibj.CANTalon;
import frc.team5333.core.control.strategy.StrategyController;
import frc.team5333.core.data.DefenseInfo;
import frc.team5333.core.hardware.IO;
import frc.team5333.core.systems.Systems;
import frc.team5333.webui.WebHandler;
import jaci.openrio.toast.core.shared.GlobalBlackboard;
import jaci.openrio.toast.lib.math.MathHelper;
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
        obj.put("Left Throttle", p(IO.motor_master_left.get() * 100));
        obj.put("Right Throttle", p(IO.motor_master_right.get() * 100));

        obj.put("Flywheel Top", p(IO.motor_flywheel_top.get() * 100));
        obj.put("Flywheel Bottom", p(IO.motor_flywheel_bottom.get() * 100));

        obj.put("Strategy", StrategyController.INSTANCE.getStrategy().getName());

        obj.put("Throttle Scale", p(Systems.drive.getThrottleScale() * 100));

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(WebHandler.jsonToString(obj));
            } catch (IOException e) {}
        });
    }

    public static String p(double d) {
        return String.format("%.2f%%", d);
    }
}

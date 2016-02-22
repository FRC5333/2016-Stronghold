package frc.team5333.webui.websockets;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import frc.team5333.core.Core;
import frc.team5333.core.control.strategy.StrategyController;
import frc.team5333.core.control.strategy.StrategyShoot;
import frc.team5333.core.systems.Systems;
import frc.team5333.core.vision.VisionFrameEvent;
import frc.team5333.core.vision.VisionNetwork;
import frc.team5333.lib.events.EventBus;
import frc.team5333.lib.events.EventListener;
import frc.team5333.webui.WebHandler;
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
public class SocketTraining {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public static void init() {
        EventBus.INSTANCE.register(SocketTraining.class);
    }

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
        try {
            JsonObject obj = JsonParser.object().from(message);
            String action = obj.getString("action");
            if (action.equalsIgnoreCase("test")) {
                double top = obj.getDouble("top");
                double btm = obj.getDouble("btm");

                // Test Strategy
                StrategyShoot strat = new StrategyShoot(top, btm);
                strat.then(StrategyController.INSTANCE.getStrategy());
                StrategyController.INSTANCE.setStrategy(strat);
            } else if (action.equalsIgnoreCase("register")) {
                double distance = VisionNetwork.INSTANCE.getActive().getDepth_mm();
                double top = obj.getDouble("top");
                double btm = obj.getDouble("btm");

                Systems.shooter.getLOOKUP().addEntry(distance, top, btm);
                Systems.shooter.sync();
            } else if (action.equalsIgnoreCase("clear")) {
                Systems.shooter.getLOOKUP().clearEntries();
                Systems.shooter.sync();
            }
        } catch (JsonParserException e) { }
    }

    @EventListener
    public static void onEvent(VisionFrameEvent event) {
        sessions.forEach(session -> {
            try {
                if (event.getFrame() == null)
                    session.getRemote().sendString("null");
                else {
                    session.getRemote().sendString(String.format("%.2f", event.getFrame().getDepth_mm() / 1000.0));
                }
            } catch (IOException e) {}
        });
    }
}

package frc.team5333.core.vision;

import frc.team5333.core.Core;
import frc.team5333.core.network.NetworkHub;
import frc.team5333.lib.events.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public enum VisionNetwork {
    INSTANCE;

    Thread t;
    VisionFrame activeFrame;

    public void setActive(VisionFrame active) {
        activeFrame = active;
        EventBus.INSTANCE.raiseEvent(new VisionFrameEvent(active));
    }

    public VisionFrame getActive() {
        return activeFrame;
    }

    public void init() {
        t = new Thread(() -> {
            while (true) {
                Socket active = NetworkHub.PROCESSORS.KINECT.getActive();
                if (active == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) { }
                } else {
                    try {
                        read(active);
                    } catch (Exception e) {
                        NetworkHub.PROCESSORS.KINECT.setActive(null);
                        Core.logger.exception(e);
                    }
                }
            }
        });
        t.start();
    }

    public static void read(Socket s) throws IOException {
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();

        while (true) {
            int i = NetworkHub.INSTANCE.readInt(in);
            if (i == 0xBA) {
                i = NetworkHub.INSTANCE.readInt(in);
                VisionFrame activeFrame = null;
                while (i == 0xBB) {
                    VisionFrame frame = new VisionFrame();
                    i = NetworkHub.INSTANCE.readInt(in);
                    frame.setX(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    frame.setY(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    frame.setWidth(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    frame.setHeight(i);

                    frame.fin();

                    if (activeFrame == null || frame.area() > activeFrame.area()) activeFrame = frame;      // Select largest area
                }

                VisionNetwork.INSTANCE.setActive(activeFrame);
            }
        }
    }

}

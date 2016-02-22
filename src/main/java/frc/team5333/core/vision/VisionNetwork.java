package frc.team5333.core.vision;

import frc.team5333.core.Core;
import frc.team5333.core.network.NetworkHub;
import frc.team5333.core.network.NetworkHubEvent;
import frc.team5333.lib.events.EventBus;
import frc.team5333.lib.events.EventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public enum VisionNetwork {
    INSTANCE;

    Thread t;

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
                while (i == 0xBB) {
                    VisionRectangle r = new VisionRectangle();
                    i = NetworkHub.INSTANCE.readInt(in);
                    r.setX(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    r.setY(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    r.setWidth(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    r.setHeight(i);

                    i = NetworkHub.INSTANCE.readInt(in);
                    r.setDepth_mm(i);

                    Core.logger.info("Rectangle Found!");
                }
            }
        }
    }

}

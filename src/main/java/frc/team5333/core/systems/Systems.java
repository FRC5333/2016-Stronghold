package frc.team5333.core.systems;

import frc.team5333.core.control.ControlLease;
import frc.team5333.core.hardware.IO;
import frc.team5333.core.systems.ControlSystem;
import frc.team5333.core.systems.DriveSystem;

public class Systems {

    public static ControlSystem control;
    public static DriveSystem drive;
    public static ShooterSystem shooter;

    public static void init() {
        control = new ControlSystem();
        drive   = new DriveSystem(IO.motor_master_left, IO.motor_master_right);
        shooter = new ShooterSystem(IO.motor_flywheel_top, IO.motor_flywheel_bottom, IO.motor_intake);
        shooter.init();
    }

}

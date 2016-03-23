package frc.team5333.core.systems;

import frc.team5333.core.hardware.IO;

public class Systems {

    public static ControlSystem control;
    public static DriveSystem drive;
    public static ShooterSystem shooter;

    public static void init() {
        control = new ControlSystem();
        drive   = new DriveSystem(IO.motor_master_left, IO.motor_slave_left, IO.motor_master_right, IO.motor_slave_right);
        shooter = new ShooterSystem(IO.motor_flywheel_top, IO.motor_flywheel_bottom, IO.motor_intake);
        shooter.init();
    }

    /**
     * Tick all Systems. This will only go through during Autonomous / Teleoperated periods.
     */
    public static void tick() {
        control.tick();
        drive.tick();
        shooter.tick();
    }

}

package frc.team5333.core.motion.spline;

public class Trajectory {

    public static class Section {

        public double position, velocity, acceleration, jerk, heading, time, x, y;

        public Section() { }

        public Section(double pos, double vel, double acc, double jerk, double head, double time, double x, double y) {
            this.position = pos; this.velocity = vel; this.acceleration = acc; this.jerk = jerk; this.heading = head;
            this.time = time; this.x = x; this.y = y;
        }

        public Section(Section to_copy) {
            position        = to_copy.position;
            velocity        = to_copy.velocity;
            acceleration    = to_copy.acceleration;
            jerk            = to_copy.jerk;
            heading         = to_copy.heading;
            time            = to_copy.time;
            x               = to_copy.x;
            y               = to_copy.y;
        }

    }

    Section[] sections;

    public Trajectory(int length) {
        sections = new Section[length];
        for (int i = 0; i < length; i++) {
            sections[i] = new Section();
        }
    }

    public Trajectory(Section[] sections) {
        this.sections = sections;
    }

    public Trajectory copy() {
        Trajectory newTraj = new Trajectory(length());
        for (int i = 0; i < length(); i++) {
            newTraj.set(i, new Section(get(i)));
        }
        return newTraj;
    }

    public int length() {
        return sections.length;
    }

    public Section get(int index) {
        return sections[index];
    }

    public void set(int index, Section section) {
        sections[index] = section;
    }

    public Section[] getAll() {
        return sections;
    }

    public void append(Trajectory to_append) {
        Section[] temp = new Section[length()
                + to_append.length()];

        for (int i = 0; i < length(); ++i)
            temp[i] = new Section(sections[i]);

        for (int i = 0; i < to_append.length(); ++i)
            temp[i + length()] = new Section(to_append.get(i));

        this.sections = temp;
    }

}

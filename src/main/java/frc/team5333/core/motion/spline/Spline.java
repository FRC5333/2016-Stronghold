package frc.team5333.core.motion.spline;

public class Spline {

    // Follows spline formula (ax^5 + bx^4 + cx^3 + dx^2 + ex). Coefficient groups
    public double a5;
    public double b4;
    public double c3;
    public double d2;
    public double e1;

    // Real-world offsets
    public double x_offset;
    public double y_offset;
    public double angle_offset;
    public double knot_distance;
    public double arc_length;

    public double[] getXandY(double percentage) {
        double[] result = new double[2];

        percentage = Math.max(Math.min(percentage, 1), 0);
        double x_val = percentage * knot_distance;
        double y_val = (a5 * x_val + b4) * x_val * x_val * x_val * x_val
                + c3 * x_val * x_val * x_val + d2 * x_val * x_val + e1 * x_val;

        double cos_theta = Math.cos(angle_offset);
        double sin_theta = Math.sin(angle_offset);

        result[0] = x_val * cos_theta - y_val * sin_theta + x_offset;
        result[1] = x_val * sin_theta + y_val * cos_theta + y_offset;
        return result;
    }

    public String toString() {
        return String.format("Spline[%.2fx^5 + %.2fx^4 + %.2fx^3 + %.2fx^2 + %.2fx][x=%.2f, y=%.2f, theta=%.2f, knot=%.2f, arc=%.2f]",
                a5, b4, c3, d2, e1, x_offset, y_offset, angle_offset, knot_distance, arc_length);
    }

    public String asFormula(int dp) {
        double x_offset = this.x_offset - knot_distance;
        return String.format("%."+dp+"f(x - %."+dp+"f)^5 + %."+dp+"f(x - %."+dp+"f)^4 + %."+dp+"f(x - %."+dp+"f)^3 + %."+dp+"f(x - %."+dp+"f)^2 + %."+dp+"f(x - %."+dp+"f) + %."+dp+"f",
                a5, x_offset, b4, x_offset, c3, x_offset, d2, x_offset, e1, x_offset, y_offset);
    }

}

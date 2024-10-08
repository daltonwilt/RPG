package com.outcast.rpg.util.math;

import org.bukkit.util.Vector;

public final class MathUtil {

    /**
     * An arbitrarily small number against which any double value subject to inaccuracy may be safely compared.
     * Use {@link #zero(double)} or {@link Math#abs(double)} <= {@link #EPSILON}.
     */
    public static final double EPSILON = 1e-6;

    /**
     * Compares the provided value with {@link #EPSILON} rather than 0.0 in order to
     * mitigate slight inaccuracy, such as with velocities.
     * @param value The value to compare.
     * @return Whether the provided value is effectively 0.0.
     */
    public static boolean zero(double value) {
        return Math.abs(value) <= EPSILON;
    }

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Clamping method.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Linear interpolation methods.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static double lerp(double from, double to, double ratio) {
        ratio = MathUtil.clamp(ratio, 0.0, 1.0);
        return from + (to - from) * ratio;
    }

    public static double lerp(double from, double to, double delta, double totalTime) {
        return lerp(from, to, delta / totalTime);
    }

    public static Vector lerp(Vector from, Vector to, double ratio) {
        double x = lerp(from.getX(), to.getX(), ratio);
        double y = lerp(from.getY(), to.getY(), ratio);
        double z = lerp(from.getZ(), to.getZ(), ratio);
        return new Vector(x, y, z);
    }

    public static Vector lerp(Vector from, Vector to, double delta, double totalTime) {
        return lerp(from, to, delta / totalTime);
    }

}

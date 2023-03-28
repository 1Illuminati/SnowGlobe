package org.red.globe.util;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class RotateHelper {
    private static final Map<Double, Double> sinXZ = new HashMap<>();
    private static final Map<Double, Double> cosXZ = new HashMap<>();
    private static final Map<Double, Double> sinY = new HashMap<>();
    private static final Map<Double, Double> cosY = new HashMap<>();

    private RotateHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void rotateX(Vector v, double angle) {
        RotateHelper.rotateAroundAxisX(v, cosXZ.computeIfAbsent(angle, a -> Math.cos(Math.toRadians(a))),
                sinXZ.computeIfAbsent(angle, a -> Math.sin(Math.toRadians(a))));
    }

    public static void rotateY(Vector v, double angle) {
        RotateHelper.rotateAroundAxisY(v, cosY.computeIfAbsent(angle, a -> Math.cos(Math.toRadians(-a))),
                sinY.computeIfAbsent(angle, a -> Math.sin(Math.toRadians(-a))));
    }

    public static void rotateZ(Vector v, double angle) {
        RotateHelper.rotateAroundAxisZ(v, cosXZ.computeIfAbsent(angle, a -> Math.cos(Math.toRadians(a))),
                sinXZ.computeIfAbsent(angle, a -> Math.sin(Math.toRadians(a))));
    }

    public static void rotate(Vector v, double angleX, double angleY, double angleZ) {
        rotateX(v, angleX);
        rotateY(v, angleY);
        rotateZ(v, angleZ);
    }
    private static void rotateAroundAxisX(Vector v, double cos, double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        v.setY(y).setZ(z);
    }

    private static void rotateAroundAxisY(Vector v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        v.setX(x).setZ(z);
    }

    private static void rotateAroundAxisZ(Vector v, double cos, double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        v.setX(x).setY(y);
    }
}

package org.red.globe.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RotateHelper {
    private static final List<Double> sinXZ = new ArrayList<>();
    private static final List<Double> cosXZ = new ArrayList<>();
    private static final List<Double> sinY = new ArrayList<>();
    private static final List<Double> cosY = new ArrayList<>();
    private static boolean block = false;

    private RotateHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlock() {
        return block;
    }

    public static void rotateX(Vector v, double angle) {
        RotateHelper.rotate(v, angle, 0, 0);
    }

    public static void rotateY(Vector v, double angle) {
        RotateHelper.rotate(v, 0, angle, 0);
    }

    public static void rotateZ(Vector v, double angle) {
        RotateHelper.rotate(v, 0, 0, angle);
    }

    public static void rotate(Vector v, double angleX, double angleY, double angleZ) {
        if (block)
            throw new IllegalStateException("Can't call this method in block");

        int angleIntX = (int) Math.floor(angleX * 100);
        int angleIntY = (int) Math.floor(angleY * 100);
        int angleIntZ = (int) Math.floor(angleZ * 100);

        RotateHelper.rotateAroundAxisX(v, cosXZ.get(angleIntX), sinXZ.get(angleIntX));
        RotateHelper.rotateAroundAxisY(v, cosY.get(angleIntY), sinY.get(angleIntY));
        RotateHelper.rotateAroundAxisZ(v, cosXZ.get(angleIntZ), sinXZ.get(angleIntZ));
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

    public static void init() {
        block = true;

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for(double i = 0.01; i <= 360; i+=0.01) {
                sinXZ.add(Math.sin(Math.toRadians(i)));
                cosXZ.add(Math.cos(Math.toRadians(i)));
                sinY.add(Math.sin(Math.toRadians(-i)));
                cosY.add(Math.cos(Math.toRadians(-i)));
            }

            block = false;
        });

        future.join();
    }

}

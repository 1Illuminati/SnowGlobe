package org.red.globe.block;

import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;
import org.red.globe.util.RotateHelper;

public record SnowGlobeBlock(Vector location, BlockState blockState) {
    public void rotation(double x, double y, double z) {
        RotateHelper.rotate(location, x, y, z);
    }

    public String toString() {
        return String.format("SnowGlobeBlock{location=%f %f %f blockState= %s}", location.getX(), location.getY(), location.getZ(), blockState.toString());
    }
}

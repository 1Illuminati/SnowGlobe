package org.red.globe.block;

import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

public class SnowGlobeBlock {
    private Vector location;

    private BlockState blockState;

    public SnowGlobeBlock(Vector location, BlockState blockState) {
        this.location = location;
        this.blockState = blockState;
    }

    public Vector getLocation() {
        return this.location;
    }

    public BlockState getState() {
        return this.blockState;
    }

    public void rotation(double yaw, double pitch) {

    }
}

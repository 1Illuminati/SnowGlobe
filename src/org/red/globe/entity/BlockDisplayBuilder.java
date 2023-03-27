package org.red.globe.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class BlockDisplayBuilder {
    private final Location location;
    private final World world;
    private Vector3f scale = new Vector3f(1);
    private Vector3f translation = new Vector3f(0);
    private AxisAngle4f leftRotation = new AxisAngle4f();
    private AxisAngle4f rightRotation = new AxisAngle4f();
    public BlockDisplayBuilder(Location location) {
        this.location = location;
        this.world = location.getWorld();
    }

    public BlockDisplayBuilder setLeftRotation(float angle, float x, float y, float z) {
        return this.setLeftRotation(new AxisAngle4f(angle, x, y, z));
    }

    public BlockDisplayBuilder setLeftRotation(AxisAngle4f leftRotation) {
        this.leftRotation = leftRotation;
        return this;
    }

    public BlockDisplayBuilder setRightRotation(float angle, float x, float y, float z) {
        return this.setRightRotation(new AxisAngle4f(angle, x, y, z));
    }

    public BlockDisplayBuilder setRightRotation(AxisAngle4f rightRotation) {
        this.rightRotation = rightRotation;
        return this;
    }

    public BlockDisplayBuilder setTranslation(float d) {
        return this.setTranslation(new Vector3f(d));
    }

    public BlockDisplayBuilder setTranslation(float x, float y, float z) {
        return this.setTranslation(new Vector3f(x, y, z));
    }

    public BlockDisplayBuilder setTranslation(Vector3f translation) {
        this.translation = translation;
        return this;
    }

    public BlockDisplayBuilder setScale(float d) {
        return this.setScale(new Vector3f(d));
    }

    public BlockDisplayBuilder setScale(float x, float y, float z) {
        return this.setScale(new Vector3f(x, y, z));
    }

    public BlockDisplayBuilder setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public BlockDisplay spawn() {
        BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
        blockDisplay.setTransformation(new Transformation(translation, leftRotation, scale, rightRotation));
        return blockDisplay;
    }
}

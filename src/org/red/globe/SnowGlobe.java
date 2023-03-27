package org.red.globe;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.red.globe.block.SnowGlobeBlock;
import org.red.globe.entity.BlockDisplayBuilder;

import java.util.ArrayList;
import java.util.List;

public class SnowGlobe {
    private final String name;
    private final Location start;
    private final Location end;
    private final List<Block> blocks = new ArrayList<>();
    private final List<SnowGlobeBlock> snowGlobeBlocks = new ArrayList<>();
    private final Vector size;
    private Vector3f scale;
    private double rotation = 0;

    public SnowGlobe(String name, Location start, Location end) {
        this.name = name;
        this.start = start;
        this.end = end;

        World world = start.getWorld();

        if (world != end.getWorld()) {
            throw new IllegalArgumentException("location need same world");
        }

        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());

        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        this.size = new Vector(maxX - minX, maxY - minY, maxZ - minZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();
                    blocks.add(block);

                    Vector vector = loc.clone().toVector().add(new Vector(-minX, -minY, -minZ)).toBlockVector();
                    snowGlobeBlocks.add(new SnowGlobeBlock(vector, block.getState()));
                }
            }
        }


    }

    public String getName() {
        return this.name;
    }

    public void spawn(Location location) {

        if (location == null)
            throw new NullPointerException("location is null");

        World world = location.getWorld();

        double x = location.getBlockX();
        double y = location.getBlockY();
        double z = location.getBlockZ();

        float sizeX = (float) (1F / this.size.getX());
        float sizeY = (float) (1F / this.size.getY());
        float sizeZ = (float) (1F / this.size.getZ());

        for (SnowGlobeBlock snowGlobeBlock : this.snowGlobeBlocks) {
            Vector vector = snowGlobeBlock.getLocation();
            double sgbX = vector.getX();
            double sgbY = vector.getY();
            double sgbZ = vector.getZ();

            double spawnX = x + (sgbX * sizeX);
            double spawnY = y + (sgbY * sizeY);
            double spawnZ = z + (sgbZ * sizeZ);
            Location loc = new Location(world, spawnX, spawnY, spawnZ);
            BlockDisplay blockDisplay = new BlockDisplayBuilder(loc).setScale(sizeX, sizeY, sizeZ).spawn();

            SnowGlobePlugin.sendDebugLog(String.format("SGB Vector :  %.4f, %.4f, %.4f", sgbX, sgbY, sgbZ));
            SnowGlobePlugin.sendDebugLog(String.format("Size Vector :  %.4f, %.4f, %.4f", sizeX, sizeY, sizeZ));
            SnowGlobePlugin.sendDebugLog(String.format("Spawn Location : %s, %.4f, %.4f, %.4f", world.getName(), spawnX, spawnY, spawnZ));
        }
    }
}

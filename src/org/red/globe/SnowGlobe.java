package org.red.globe;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.red.globe.block.SnowGlobeBlock;
import org.red.globe.entity.BlockDisplayBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SnowGlobe {
    private final String name;
    private final BoundingBox box;
    private final World world;
    private final List<SnowGlobeBlock> snowGlobeBlocks = new ArrayList<>();
    private final Vector size;
    private final boolean filled;
    private boolean creating = true;
    private boolean spawning = false;
    private Vector3f scale;

    public SnowGlobe(String name, Location start, Location end, boolean filled) {
        this.name = name;
        this.box = BoundingBox.of(start.toVector().toBlockVector(), end.toVector().toBlockVector());

        this.world = start.getWorld();
        if (world != end.getWorld())
            throw new IllegalArgumentException("location need same world");

        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());

        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        this.size = new Vector(maxX - minX, maxY - minY, maxZ - minZ);
        this.filled = filled;

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    for (int z = minZ; z < maxZ; z++) {
                        Location loc = new Location(world, x, y, z);
                        Block block = loc.getBlock();
                        Material type = block.getType();

                        if (type == Material.AIR || type == Material.VOID_AIR || type == Material.CAVE_AIR || (blockIsClose(block) && filled))
                            continue;

                        Vector vector = loc.clone().toVector().add(new Vector(-minX, -minY, -minZ)).toBlockVector();
                        snowGlobeBlocks.add(new SnowGlobeBlock(vector, block.getState()));
                    }
                }
            }

            creating = false;
        });

        future.join();
    }



    public boolean isCreating() {
        return this.creating;
    }

    public boolean isSpawning() {
        return this.spawning;
    }

    public String getName() {
        return this.name;
    }

    public BoundingBox getBox() {
        return this.box;
    }

    public Vector getSize() {
        return this.size;
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public World getWorld() {
        return this.world;
    }

    private boolean blockIsClose(Block block) {
        int[][] directions = {{0, 1, 0}, {0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
        Block[] nearBlocks = new Block[6];
        for (int i = 0; i < directions.length; i++) {
            nearBlocks[i] = block.getLocation().clone().add(directions[i][0], directions[i][1], directions[i][2]).getBlock();

            if (!box.contains(nearBlocks[i].getLocation().toVector().toBlockVector()))
                return false;

            if (isIgnoreBlock(nearBlocks[i].getType()))
                return false;
        }

        return true;
    }

    public void spawn(Location location) {
        if (location == null)
            throw new NullPointerException("location is null");

        spawning = true;

        World spawnWorld = location.getWorld();

        double x = location.getBlockX();
        double y = location.getBlockY();
        double z = location.getBlockZ();

        float sizeX = (float) (1F / this.size.getX());
        float sizeY = (float) (1F / this.size.getY());
        float sizeZ = (float) (1F / this.size.getZ());

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            for (SnowGlobeBlock snowGlobeBlock : this.snowGlobeBlocks) {
                Vector vector = snowGlobeBlock.location();
                double sgbX = vector.getX();
                double sgbY = vector.getY();
                double sgbZ = vector.getZ();

                double spawnX = x + (sgbX * sizeX);
                double spawnY = y + (sgbY * sizeY);
                double spawnZ = z + (sgbZ * sizeZ);
                Location loc = new Location(spawnWorld, spawnX, spawnY, spawnZ);
                BlockDisplay blockDisplay = new BlockDisplayBuilder(loc).setScale(sizeX, sizeY, sizeZ).spawn();
                blockDisplay.setBlock(snowGlobeBlock.blockState().getBlockData());

                SnowGlobePlugin.sendDebugLog(String.format("SGB Vector :  %.4f, %.4f, %.4f", sgbX, sgbY, sgbZ));
                SnowGlobePlugin.sendDebugLog(String.format("Size Vector :  %.4f, %.4f, %.4f", sizeX, sizeY, sizeZ));
                SnowGlobePlugin.sendDebugLog(String.format("Spawn Location : %s, %.4f, %.4f, %.4f", world.getName(), spawnX, spawnY, spawnZ));
            }

            spawning = false;
        });

        future.join();
    }

    private boolean isIgnoreBlock(Material material) {
        switch (material) {
            case AIR, VOID_AIR, GLASS, GLASS_PANE, GRAY_STAINED_GLASS, GREEN_STAINED_GLASS, BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS, BROWN_STAINED_GLASS,
                    LIME_STAINED_GLASS, CYAN_STAINED_GLASS, MAGENTA_STAINED_GLASS, PINK_STAINED_GLASS, ORANGE_STAINED_GLASS, RED_STAINED_GLASS, PURPLE_STAINED_GLASS,
                    TINTED_GLASS, YELLOW_STAINED_GLASS, WHITE_STAINED_GLASS, BLACK_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE,
                    PURPLE_STAINED_GLASS_PANE, GRAY_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE,
                    BROWN_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE,
                    CYAN_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE, DARK_OAK_DOOR, ACACIA_DOOR, BIRCH_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, DARK_OAK_TRAPDOOR, ACACIA_TRAPDOOR,
                    BIRCH_TRAPDOOR, JUNGLE_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, DARK_OAK_FENCE_GATE, ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, OAK_FENCE_GATE,
                    SPRUCE_FENCE_GATE, DARK_OAK_FENCE, ACACIA_FENCE, BIRCH_FENCE, JUNGLE_FENCE, OAK_FENCE, SPRUCE_FENCE, DARK_OAK_BUTTON, ACACIA_BUTTON, BIRCH_BUTTON, JUNGLE_BUTTON,
                    OAK_BUTTON, SPRUCE_BUTTON, DARK_OAK_PRESSURE_PLATE, ACACIA_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE,
                    DARK_OAK_LEAVES, ACACIA_LEAVES, BIRCH_LEAVES, JUNGLE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES, DARK_OAK_SAPLING, ACACIA_SAPLING, BIRCH_SAPLING, JUNGLE_SAPLING, OAK_SAPLING,
                    SPRUCE_SAPLING, DARK_OAK_SIGN, ACACIA_SIGN, BIRCH_SIGN, JUNGLE_SIGN, OAK_SIGN, SPRUCE_SIGN, DARK_OAK_WALL_SIGN, ACACIA_WALL_SIGN, BIRCH_WALL_SIGN, JUNGLE_WALL_SIGN,
                    OAK_WALL_SIGN, SPRUCE_WALL_SIGN, DARK_OAK_STAIRS, ACACIA_STAIRS, BIRCH_STAIRS, JUNGLE_STAIRS, OAK_STAIRS, SPRUCE_STAIRS, DARK_OAK_SLAB, ACACIA_SLAB, BIRCH_SLAB,
                    JUNGLE_SLAB, OAK_SLAB, SPRUCE_SLAB, RAIL, ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL, TORCH, REDSTONE_TORCH, REDSTONE_WALL_TORCH, WALL_TORCH, REDSTONE_WIRE,
                    REDSTONE_LAMP, REDSTONE, REPEATER, COMPARATOR, LEVER, STONE_BUTTON, STONE_PRESSURE_PLATE, IRON_BARS, IRON_DOOR, IRON_TRAPDOOR, LIGHT_WEIGHTED_PRESSURE_PLATE,
                    HEAVY_WEIGHTED_PRESSURE_PLATE, LADDER, VINE, LILY_PAD, SNOW, BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER,
                    LIGHT_GRAY_BANNER, LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PINK_BANNER, PURPLE_BANNER, RED_BANNER, WHITE_BANNER, YELLOW_BANNER, BLACK_WALL_BANNER, BLUE_WALL_BANNER,
                    BROWN_WALL_BANNER, CYAN_WALL_BANNER, GRAY_WALL_BANNER, GREEN_WALL_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_WALL_BANNER, MAGENTA_WALL_BANNER,
                    ORANGE_WALL_BANNER, PINK_WALL_BANNER, PURPLE_WALL_BANNER, RED_WALL_BANNER, WHITE_WALL_BANNER, YELLOW_WALL_BANNER, BLACK_BED, BLUE_BED, BROWN_BED, CYAN_BED, GRAY_BED,
                    GREEN_BED, LIGHT_BLUE_BED, LIGHT_GRAY_BED, LIME_BED, MAGENTA_BED, ORANGE_BED, PINK_BED, PURPLE_BED, RED_BED, WHITE_BED, YELLOW_BED, BLACK_CARPET, BLUE_CARPET, BROWN_CARPET,
                    CYAN_CARPET, GRAY_CARPET, GREEN_CARPET, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, LIME_CARPET, MAGENTA_CARPET, ORANGE_CARPET, PINK_CARPET, PURPLE_CARPET, RED_CARPET, WHITE_CARPET,
                    YELLOW_CARPET, GLOW_LICHEN -> { return true; }
            default -> { return false; }
        }
    }
}

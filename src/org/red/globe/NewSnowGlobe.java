package org.red.globe;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.red.globe.entity.BlockDisplayBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NewSnowGlobe {
    private final String name;
    private Location start;
    private Location end;
    private BoundingBox box;
    private SizeType sizeType = SizeType.LONG;
    private boolean filled = false;
    private Vector3d rotation = new Vector3d(0, 0, 0);

    public NewSnowGlobe(String name, Location start, Location end) {
        this.name = name;
        this.setLoc(start, end);
    }

    public String getName() {
        return this.name;
    }

    public void setLoc(Location start, Location end) {

        if (start.getWorld() != end.getWorld())
            throw new IllegalArgumentException("location world need to same");

        this.start = start;
        this.end = end;
        this.box = BoundingBox.of(start.toVector().toBlockVector(), end.toVector().toBlockVector());
    }

    public Location getStart() {
        return this.start;
    }

    public Location getEnd() {
        return this.end;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public SizeType getSizeType() {
        return this.sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    public Vector3d getRotation() {
        return this.rotation;
    }

    public void setRotation(double x, double y, double z) {
        this.rotation = new Vector3d(x, y, z);
    }

    public void addRotation(double x, double y, double z) {
        this.rotation.add(x, y, z);
    }

    public void clearRotation() {
        this.rotation = new Vector3d(0, 0, 0);
    }

    public void spawn(Location spawnLoc) {
        World spawnWorld = spawnLoc.getWorld();
        World originWorld = this.start.getWorld();
        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());

        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        int sizeX = maxX - minX;
        int sizeY = maxY - minY;
        int sizeZ = maxZ - minZ;

        Vector displaySize;

        if (this.sizeType == SizeType.FILLED)
            displaySize = new Vector(1F / sizeX, 1F / sizeY, 1F / sizeZ);
        else if (this.sizeType == SizeType.LONG) {
            int longest = Math.max(Math.max(sizeX, sizeY), sizeZ);
            displaySize = new Vector(1F / longest, 1F / longest, 1f / longest);
        } else
            throw new IllegalArgumentException("SizeType is Null");

        List<BlockDisplayBuilder> builders = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(() -> {
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    for (int z = minZ; z < maxZ; z++) {
                        Location blockLoc = new Location(originWorld, x, y, z);
                        Block block = blockLoc.getBlock();
                        Material type = block.getType();

                        if (type == Material.AIR || type == Material.VOID_AIR || type == Material.CAVE_AIR || (blockIsClose(block) && filled))
                            continue;

                        Vector vector = blockLoc.clone().add(-minX, -minY, -minZ).toVector().multiply(displaySize)
                                .add(spawnLoc.clone().toVector().toBlockVector());

                        builders.add(new BlockDisplayBuilder(new Location(spawnWorld, vector.getX(), vector.getY(), vector.getZ()))
                                .setScale(displaySize).setBlockData(block.getBlockData()));
                    }
                }
            }

        });

        executor.shutdown();

        try {
            future.get();

            builders.forEach(BlockDisplayBuilder::spawn);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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

    public enum SizeType {
        FILLED,
        LONG,
    }
}

package org.red.globe.entity.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }

    private final UUID uuid;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Location getPos1() {
        return (Location) this.dataMap.get("pos1");
    }

    public void setPos1(Location pos1) {
        this.dataMap.put("pos1", pos1);
    }

    public Location getPos2() {
        return (Location) this.dataMap.get("pos2");
    }

    public void setPos2(Location pos2) {
        this.dataMap.put("pos2", pos2);
    }
}

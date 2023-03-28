package org.red.globe.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.red.globe.SnowGlobe;
import org.red.globe.SnowGlobeManager;
import org.red.globe.entity.player.PlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnowGlobeCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "snowGlobe";
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        Location location = player.getLocation();
        PlayerData playerData = PlayerData.getPlayerData(player);
        switch(args[0]) {
            case "pos1" -> playerData.setPos1(location);
            case "pos2" -> playerData.setPos2(location);
            case "spawn"-> {
                SnowGlobe snowGlobe = SnowGlobeManager.getInstance().get(args[1]);
                if (snowGlobe == null) {
                    player.sendMessage("SnowGlobe not found!");
                    return false;
                }

                snowGlobe.spawn(location);
            }
            case "register" -> {
                Location pos1 = playerData.getPos1();
                Location pos2 = playerData.getPos2();
                if (pos1 == null || pos2 == null) {
                    player.sendMessage("You must set both positions first!");
                    return false;
                }

                SnowGlobe snowGlobe = new SnowGlobe(args[1], pos1, pos2, args[2].equals("true"));
                SnowGlobeManager.getInstance().register(snowGlobe);
            }
            default -> {
                player.sendMessage("Invalid argument!");
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1)
            return Arrays.asList("pos1", "pos2", "spawn", "register", "tool", "item");

        if (args.length == 3 && args[0].equals("register"))
            return Arrays.asList("true", "false");


        return new ArrayList<>();
    }
}

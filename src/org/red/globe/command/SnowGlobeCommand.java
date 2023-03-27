package org.red.globe.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1)
            return Arrays.asList("pos1", "pos2", "spawn", "register", "tool", "item");

        return new ArrayList<>();
    }
}

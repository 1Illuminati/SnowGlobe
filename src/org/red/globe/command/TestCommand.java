package org.red.globe.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.red.globe.SnowGlobe;

import java.util.ArrayList;
import java.util.List;

public class TestCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        Location loc = player.getLocation();
        int size = Integer.parseInt(args[0]);
        Location start = new Location(loc.getWorld(), loc.getBlockX() - size, loc.getBlockY() - size, loc.getBlockZ() - size);
        Location end = new Location(loc.getWorld(), loc.getBlockX() + size, loc.getBlockY() + size, loc.getBlockZ() + size);

        new SnowGlobe("test", start, end, Boolean.parseBoolean(args[1])).spawn(loc);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}

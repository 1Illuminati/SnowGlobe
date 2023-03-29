package org.red.globe.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.red.globe.NewSnowGlobe;
import org.red.globe.entity.player.PlayerData;

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
        PlayerData playerData = PlayerData.getPlayerData(player);
        Location loc = player.getLocation();

        NewSnowGlobe snowGlobe = new NewSnowGlobe("test", playerData.getPos1(), playerData.getPos2());
        snowGlobe.spawn(loc);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}

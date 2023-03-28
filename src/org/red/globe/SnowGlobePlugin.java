package org.red.globe;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.red.globe.command.AbstractCommand;
import org.red.globe.command.SnowGlobeCommand;
import org.red.globe.command.TestCommand;
import org.red.globe.util.RotateHelper;

public final class SnowGlobePlugin extends JavaPlugin {
    public static final String NAME = "SnowGlobe";
    private static SnowGlobePlugin plugin;
    private static boolean reload = false;
    private static final boolean debug = true;

    public static SnowGlobePlugin getPlugin() {
        return plugin;
    }

    public static boolean isReload() {
        return reload;
    }

    public static void sendLog(Object message) {
        Bukkit.getConsoleSender().sendMessage("§5§l[ §5" + NAME + "§5§l ]: §f" + message);
    }

    public static void sendDebugLog(Object message) {
        if (debug)
            Bukkit.getConsoleSender().sendMessage("§5§l[ §5" + NAME + " Debug" + "§5§l ]: §f" + message);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        setCommand();
        RotateHelper.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommand(AbstractCommand command) {
        PluginCommand co = this.getCommand(command.getName());

        if (co == null) {
            throw new NullPointerException("Command " + command.getName() + " not found");
        }

        co.setExecutor(command);
        co.setTabCompleter(command);
    }

    private void setCommand() {
        this.registerCommand(new SnowGlobeCommand());
        this.registerCommand(new TestCommand());
    }
}

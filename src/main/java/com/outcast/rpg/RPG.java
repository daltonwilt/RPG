package com.outcast.rpg;

import com.outcast.rpg.util.setting.PluginLoader;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class RPG extends JavaPlugin {

    private static final String CONFIG_KEY = "rpg";
    private static final Level logLevel = Level.INFO;
    private static final int serverTickRate = 20;

    @Getter
    public static RPG instance;

    public static void log(Level level, String message, Object... format) {
        log(instance, level, message, format);
    }
    public static void log(JavaPlugin plugin, Level level, String message, Object... format) {
        if (format.length > 0)
            message = String.format(message, format);
        plugin.getLogger().log(level, message);
    }

    public static void info(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.INFO, message, format);
    }
    public static void info(String message, Object... format) {
        info(instance, message, format);
    }

    public static void warn(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.WARNING, message, format);
    }
    public static void warn(String message, Object... format) {
        warn(instance, message, format);
    }

    public static void severe(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.SEVERE, message, format);
    }
    public static void severe(String message, Object... format) {
        severe(instance, message, format);
    }

    @Override
    public void onLoad() {
        instance = this;
        PluginLoader.load(this);
    }

    @Override
    public void onEnable() {
        instance = this;

        printDivider();
        printBlankLine();
        info("  RPG v%s", getDescription().getVersion());
        info("  Running on %s.", getServer().getVersion());
        printBlankLine();
        printDivider();

        getLogger().setLevel(logLevel);
        PluginLoader.setup(this);
    }

    @Override
    public void onDisable() {
        PluginLoader.disable(this);
    }

    public void printBlankLine() {
        info("");
    }
    public void printDivider() {
        info("==========================================");
    }

}

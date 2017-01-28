package tech.shadowsystems.skywars;

import org.bukkit.plugin.java.JavaPlugin;

public final class Skywars extends JavaPlugin {

    private static Skywars instance;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
    }

    @Override
    public void onDisable() {

        instance = null;
    }

    public static Skywars getInstance() {
        return instance;
    }

}

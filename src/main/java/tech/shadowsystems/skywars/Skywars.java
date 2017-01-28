package tech.shadowsystems.skywars;

import org.bukkit.plugin.java.JavaPlugin;
import tech.shadowsystems.skywars.constructors.Game;
import tech.shadowsystems.skywars.data.DataHandler;

import java.util.HashSet;
import java.util.Set;

public final class Skywars extends JavaPlugin {

    private static Skywars instance;
    private Set<Game> games = new HashSet<>();
    private int gamesLimit = 0;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        if (getConfig().getBoolean("single-server-mode")) { // If we're using single server
            gamesLimit = 1;
        } else {
            gamesLimit = -1;
        }

        if (DataHandler.getInstance().getGameInfo().getConfigurationSection("games") != null) {
            for (String gameName : DataHandler.getInstance().getGameInfo().getConfigurationSection("games").getKeys(false)) {
                Game game = new Game(gameName);
                this.registerGame(game);
            }
        } else {
            // We can assume that no games are created
            getLogger().warning("No games have been created. Please create one using the creation command.");
        }
    }

    @Override
    public void onDisable() {

        instance = null;
    }

    public static Skywars getInstance() {
        return instance;
    }

    public boolean registerGame(Game game) {
        if (games.size() == gamesLimit && gamesLimit != -1) { // If we're at our limit, don't add a game
            return false;
        }

        games.add(game);

        return true;
    }

}

/*
- Bungee Support AND one server support
 */
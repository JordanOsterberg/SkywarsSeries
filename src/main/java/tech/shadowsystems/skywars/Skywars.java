package tech.shadowsystems.skywars;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tech.shadowsystems.skywars.commands.SkywarsCommand;
import tech.shadowsystems.skywars.listeners.*;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.data.DataHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Skywars extends JavaPlugin {

    private static Skywars instance;
    private Set<Game> games = new HashSet<>();
    private int gamesLimit = 0;
    private boolean isSingleServerMode = false;

    private Map<Player, Game> playerGameMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        this.isSingleServerMode = getConfig().getBoolean("single-server-mode");

        if (this.isSingleServerMode) { // If we're using single server
            gamesLimit = 1;
        } else {
            gamesLimit = getConfig().getInt("max-games");
        }

        if (DataHandler.getInstance().getGameInfo().getConfigurationSection("games") != null) {
            for (String gameName : DataHandler.getInstance().getGameInfo().getConfigurationSection("games").getKeys(false)) {
                Game game = new Game(gameName);
                boolean status = this.registerGame(game);
                if (!status) {
                    getLogger().warning("Can't load game " + gameName + "! Reached game limit for this server.");
                }
            }
        } else {
            // We can assume that no games are created
            getLogger().warning("No games have been created. Please create one using the creation command.");
        }

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new FoodLevel(), this);
        getServer().getPluginManager().registerEvents(new ChestInteract(), this);
        getServer().getPluginManager().registerEvents(new BlockInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        getServer().getPluginManager().registerEvents(new EntitySpawn(), this);

        getCommand("skywars").setExecutor(new SkywarsCommand());

        if (isSingleServerMode()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }
    }

    @Override
    public void onDisable() {
        for (Map.Entry<Player, Game> entry : playerGameMap.entrySet()) {
            entry.getKey().teleport(getLobbyPoint());
            entry.getKey().getInventory().clear();
            entry.getKey().getInventory().setArmorContents(null);
            entry.getKey().setHealth(entry.getKey().getMaxHealth());
            entry.getKey().setGameMode(GameMode.SURVIVAL);
        }

        if (isSingleServerMode()) {
            for (Player player : getServer().getOnlinePlayers()) {
                player.teleport(getLobbyPoint());
            }
        }

        // Rollback every game

        for (Game game : getGames()) {
            for (Player player : game.getWorld().getPlayers()) {
                player.teleport(getLobbyPoint());
            }

            RollbackHandler.getRollbackHandler().rollback(game.getWorld());
        }

        instance = null;
    }


    private Location lobbyPoint = null;
    public Location getLobbyPoint() {
        if (lobbyPoint == null) {
            int x = 0;
            int y = 0;
            int z = 0;
            String world = "world";

            try {
                x = Skywars.getInstance().getConfig().getInt("lobby-point.x");
                y = Skywars.getInstance().getConfig().getInt("lobby-point.y");
                z = Skywars.getInstance().getConfig().getInt("lobby-point.z");
                world = Skywars.getInstance().getConfig().getString("lobby-point.world");
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe("Lobby point failed with exception: " + ex);
                ex.printStackTrace();
            }

            lobbyPoint = new Location(Bukkit.getWorld(world), x, y, z);
        }

        return lobbyPoint;
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

    public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getDisplayName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }

        return null;
    }

    public Game getGame(Player player) {
        return this.playerGameMap.get(player);
    }

    public void setGame(Player player, Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        } else {
            this.playerGameMap.put(player, game);
        }
    }

    public boolean isSingleServerMode() {
        return isSingleServerMode;
    }

    public Set<Game> getGames() {
        return games;
    }
}
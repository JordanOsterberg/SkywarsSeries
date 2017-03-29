package tech.shadowsystems.skywars.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.data.DataHandler;
import tech.shadowsystems.skywars.tasks.GameCountdownTask;

import java.util.*;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class Game {

    // Basic config options
    private String displayName;
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private List<Location> spawnPoints;
    private boolean isTeamGame;
    private Location lobbyPoint;

    // Active game information
    private Set<GamePlayer> players;
    private Set<GamePlayer> spectators;
    private GameState gameState = GameState.LOBBY;
    private Map<GamePlayer, Location> gamePlayerToSpawnPoint = new HashMap<>();

    public Game(String gameName) {
        FileConfiguration fileConfiguration = DataHandler.getInstance().getGameInfo();

        this.displayName = fileConfiguration.getString("games." + gameName + ".displayName");
        this.maxPlayers = fileConfiguration.getInt("games." + gameName + ".maxPlayers");
        this.minPlayers = fileConfiguration.getInt("games." + gameName + ".minPlayers");
        this.world = Bukkit.createWorld(new WorldCreator(fileConfiguration.getString("games." + gameName + ".worldName")));

        try {
            String[] values = fileConfiguration.getString("games." + gameName + ".lobbyPoint").split(","); // [X:0, Y:0, Z:0]
            double x = Double.parseDouble(values[0].split(":")[1]); // X:0 -> X, 0 -> 0
            double y = Double.parseDouble(values[1].split(":")[1]);
            double z = Double.parseDouble(values[2].split(":")[1]);
            lobbyPoint = new Location(world, x, y, z);
        } catch (Exception ex) {
            Skywars.getInstance().getLogger().severe("Failed to load lobbyPoint with metadata " + fileConfiguration.getString("games." + gameName + ".lobbyPoint") + " for gameName: '" + gameName + "'. ExceptionType: " + ex);
        }

        this.spawnPoints = new ArrayList<>();

        for (String point : fileConfiguration.getStringList("games." + gameName + ".spawnPoints")) {
            // X:0,Y:0,Z:0
            try {
                String[] values = point.split(","); // [X:0, Y:0, Z:0]
                double x = Double.parseDouble(values[0].split(":")[1]); // X:0 -> X, 0 -> 0
                double y = Double.parseDouble(values[1].split(":")[1]);
                double z = Double.parseDouble(values[2].split(":")[1]);
                Location location = new Location(world, x, y, z);
                spawnPoints.add(location);
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe("Failed to load spawnPoint with metadata " + point + " for gameName: '" + gameName + "'. ExceptionType: " + ex);
            }
        }

        this.isTeamGame = fileConfiguration.getBoolean("games." + gameName + ".isTeamGame");
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
    }

    public boolean joinGame(GamePlayer gamePlayer) {
        if (gamePlayer.isTeamClass() && !isTeamGame) {
            return false;
        }

        if (isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            if (getPlayers().size() == getMaxPlayers()) {
                gamePlayer.sendMessage("&c[!] This game is full.");
                return false;
            }

            getPlayers().add(gamePlayer);
            gamePlayer.teleport(isState(GameState.LOBBY) ? lobbyPoint : null);
            sendMessage("&a[+] &6" + gamePlayer.getName() + " &7(" + getPlayers().size() + "&a/&7" + getMaxPlayers() + ")");

            if (getPlayers().size() == getMinPlayers() && !isState(GameState.STARTING)) {
                setState(GameState.STARTING);
                sendMessage("&a[*] The game will begin in 20 seconds...");
                startCountdown();
            }

            return true;
        } else {
            getSpectators().add(gamePlayer);
            // TODO: process as spectator
            return true;
        }
    }

    public void startCountdown() {
        int id = 0;
        for (GamePlayer gamePlayer : getPlayers()) {
            try {
                gamePlayerToSpawnPoint.put(gamePlayer, spawnPoints.get(id));
                gamePlayer.teleport(spawnPoints.get(id));
                id += 1;
            } catch (IndexOutOfBoundsException ex) {
                Skywars.getInstance().getLogger().severe("Not enough spawn points to satisfy game needs (Game is " + getDisplayName() + ")");
            }
        }

        new GameCountdownTask(this).runTaskTimer(Skywars.getInstance(), 0, 20);
    }

    public boolean isState(GameState state) {
        return getGameState() == state;
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<GamePlayer> getPlayers() {
        return players;
    }

    public Set<GamePlayer> getSpectators() {
        return spectators;
    }

    public boolean isTeamGame() {
        return isTeamGame;
    }

    public void sendMessage(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    public enum GameState {
        LOBBY, STARTING, ACTIVE, DEATHMATCH, ENDING
    }

}

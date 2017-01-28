package tech.shadowsystems.skywars.constructors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import tech.shadowsystems.skywars.data.DataHandler;

import java.util.Set;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class Game {

    // Basic config options
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private Set<Location> spawnPoints;

    // Active game information
    private Set<GamePlayer> players;
    private Set<GamePlayer> spectators;
    private boolean isTeamGame;

    public Game(String gameName) {
        FileConfiguration fileConfiguration = DataHandler.getInstance().getGameInfo();

        this.maxPlayers = fileConfiguration.getInt("games." + gameName + ".maxPlayers");
        this.minPlayers = fileConfiguration.getInt("games." + gameName + ".minPlayers");
        this.world = Bukkit.createWorld(new WorldCreator(fileConfiguration.getString("games." + gameName + ".worldName")));
        // TODO spawn points
    }

}

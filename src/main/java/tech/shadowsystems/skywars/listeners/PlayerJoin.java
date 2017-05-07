package tech.shadowsystems.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.object.GamePlayer;
import tech.shadowsystems.skywars.utility.ChatUtil;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!Skywars.getInstance().getConfig().getBoolean("single-server-mode")) {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(25);

            int x = 0;
            int y = 0;
            int z = 0;
            String world;

            try {
                x = Skywars.getInstance().getConfig().getInt("lobby-point.x");
                y = Skywars.getInstance().getConfig().getInt("lobby-point.y");
                z = Skywars.getInstance().getConfig().getInt("lobby-point.z");
                world = Skywars.getInstance().getConfig().getString("lobby-point.world");
                player.teleport(new Location(Bukkit.getWorld(world), x, y, z));
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe("Lobby point failed with exception: " + ex);
                ex.printStackTrace();
            }

        }

        Game exampleGame = Skywars.getInstance().getGame("example");
        if (exampleGame == null) {
            player.sendMessage(ChatUtil.format("&c[!] &fSomething went wrong."));
            return;
        }

        exampleGame.joinGame(new GamePlayer(player));
    }

}

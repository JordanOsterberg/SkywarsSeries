package tech.shadowsystems.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.object.GamePlayer;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(null);

        Game game = Skywars.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);

            if (gamePlayer.isTeamClass()) {
                if (gamePlayer.getTeam().isPlayer(player)) {
                    player.damage(player.getMaxHealth()); // Kill player to make game process this as a death
                }
            } else {
                if (gamePlayer.getPlayer() == player) {
                    player.damage(player.getMaxHealth()); // Kill player to make game process this as a death
                }
            }
        }
    }

}

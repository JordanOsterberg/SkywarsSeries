package tech.shadowsystems.skywars.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.object.GamePlayer;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        for (Game game : Skywars.getInstance().getGames()) {
            for (GamePlayer gamePlayer : game.getPlayers()) {
                if (gamePlayer.isTeamClass()) {
                    if (gamePlayer.getTeam().isPlayer(player)) {
                        handle(event, game);
                    }
                } else {
                    if (gamePlayer.getPlayer() == player) {
                        handle(event, game);
                    }
                }
            }
        }
    }

    private void handle(PlayerDeathEvent event, Game game) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = game.getGamePlayer(player);

        event.setDeathMessage(null);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setGameMode(GameMode.SPECTATOR);

        if (gamePlayer != null) {
            game.switchToSpectator(gamePlayer);
        }
    }

}

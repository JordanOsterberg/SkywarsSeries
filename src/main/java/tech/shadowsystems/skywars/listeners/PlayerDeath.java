package tech.shadowsystems.skywars.listeners;

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

        Game game = Skywars.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);

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

    private void handle(PlayerDeathEvent event, Game game) {
        Player player = event.getEntity();

        if (!game.isState(Game.GameState.ACTIVE) && !game.isState(Game.GameState.DEATHMATCH)) {
            return;
        }

        event.setDeathMessage(null);
        game.activateSpectatorSettings(player);

        if (game.getPlayers().size() <= 1) {
            try {
                GamePlayer winner = game.getPlayers().get(0);
                if (winner.isTeamClass()) {
                    game.sendMessage("&a"); // Broadcast team win
                } else {
                    game.sendMessage("&a" + winner.getName() + " won the game!"); // Indiv win
                }

                game.setState(Game.GameState.ENDING);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

}

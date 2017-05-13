package tech.shadowsystems.skywars.listeners;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.object.GamePlayer;

import java.util.Random;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class ChestInteract implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Game game = Skywars.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.PREPARATION) || game.isState(Game.GameState.STARTING)) {
                event.setCancelled(true);
                return;
            }

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

    private void handle(PlayerInteractEvent event, Game game) {
        if (event.hasBlock() && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();

            if (game.getOpened().contains(chest) || game.getRareItems().size() == 0 || game.getNormalItems().size() == 0) {
                return;
            }

            chest.getBlockInventory().clear();

            if (new Random().nextFloat() < 0.20) {
                int toFill = new Random().nextInt(8);
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getRareItems().size());
                    chest.getBlockInventory().addItem(game.getRareItems().get(selected));
                }
            } else {
                int toFill = new Random().nextInt(5);
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getNormalItems().size());
                    chest.getBlockInventory().addItem(game.getNormalItems().get(selected));
                }
            }

            game.getOpened().add(chest);
        }
    }

}

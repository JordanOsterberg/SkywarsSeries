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

        for (Game game : Skywars.getInstance().getGames()) {
            for (GamePlayer gamePlayer : game.getPlayers()) {
                if (!gamePlayer.isTeamClass()) {
                    if (gamePlayer.getPlayer() == player) {
                        handle(event, game);
                    }
                } else {
                    if (gamePlayer.getTeam().isPlayer(player)) {
                        handle(event, game);
                    }
                }
            }
        }
    }

    private void handle(PlayerInteractEvent event, Game game) {
        if (event.hasBlock() && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();

            if (game.getOpened().contains(chest)) {
                return;
            }

            Random random = new Random();
            if (random.nextFloat() < 0.20) {
                int toFill = random.nextInt(8);
                for (int x = 0; x < toFill; x++) {
                    chest.getBlockInventory().addItem(game.getRareItems().get(random.nextInt(game.getRareItems().size())));
                }
            } else {
                int toFill = random.nextInt(5);
                for (int x = 0; x < toFill; x++) {
                    chest.getBlockInventory().addItem(game.getNormalItems().get(random.nextInt(game.getNormalItems().size())));
                }
            }
        }
    }

}

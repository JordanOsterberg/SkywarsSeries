package tech.shadowsystems.skywars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import tech.shadowsystems.skywars.object.Game;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class GameRunTask extends BukkitRunnable {

    private Game game;
    private int startIn = 10;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        this.game.assignSpawnPositions();
        this.game.sendMessage("&6[!] You've been teleported.");
        this.game.sendMessage("&a[*] The game will begin in " + this.startIn + " seconds...");
        this.game.setMovementFrozen(true);
    }

    @Override
    public void run() {
        if (startIn <= 1) {
            this.cancel();
            this.game.setState(Game.GameState.ACTIVE);
            this.game.sendMessage("&a[!] The game has started.");
            this.game.setMovementFrozen(false);
        } else {
            startIn -= 1;
            this.game.sendMessage("&c[*] The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s"));
        }
    }
}

package tech.shadowsystems.skywars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import tech.shadowsystems.skywars.object.Game;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class GameCountdownTask extends BukkitRunnable {

    private int time = 20;
    private Game game;

    public GameCountdownTask(Game game) {
         this.game = game;
    }

    @Override
    public void run() {
        time -= 1;

        if (time == 0) {
            // Start
            game.sendMessage("&6[!] The game has started!");

            cancel();
            // TODO: Run game task
        } else {
            if (time == 15 || time == 10 || time == 5) {
                game.sendMessage("&a[*] The game will begin in " + time + " seconds");
            }
        }
    }
}

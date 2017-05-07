package tech.shadowsystems.skywars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import tech.shadowsystems.skywars.Skywars;
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
            game.sendMessage("&6[!] You've been teleported.");

            cancel();
            Skywars.getInstance().getServer().getScheduler().runTask(Skywars.getInstance(), new GameRunTask(game));
        } else {
            if (time == 15 || time == 10 || time == 5) {
                game.sendMessage("&a[*] You'll be teleported to the game in " + time + " seconds");
            }
        }
    }

}

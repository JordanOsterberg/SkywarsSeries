package tech.shadowsystems.skywars.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.object.GamePlayer;
import tech.shadowsystems.skywars.utility.ChatUtil;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class JoinCommand extends SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 0) {
                player.sendMessage(ChatUtil.format("&9Skywars &7>> &c/skywars join <game name>"));
            } else {
                for (Game game : Skywars.getInstance().getGames()) {
                    for (GamePlayer gamePlayer : game.getPlayers()) {
                        if (gamePlayer.isTeamClass()) {
                            if (gamePlayer.getTeam().isPlayer(player)) {
                                player.sendMessage(ChatUtil.format("&9Skywars &7>> &cYou're in a game."));
                                return;
                            }
                        } else {
                            if (gamePlayer.getPlayer() == player) {
                                player.sendMessage(ChatUtil.format("&9Skywars &7>> &cYou're in a game."));
                                return;
                            }
                        }
                    }
                }

                Game game = Skywars.getInstance().getGame(args[0]);
                if (game == null) {
                    player.sendMessage(ChatUtil.format("&9Skywars &7>> &cThat game doesn't exist."));
                    return;
                }

                game.joinGame(new GamePlayer(player));
            }
        } else {
            commandSender.sendMessage(ChatUtil.format("&9Skywars &7>> &cYou're not a player!"));
        }
    }
}

package tech.shadowsystems.skywars.commands;

import org.bukkit.command.CommandSender;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.object.Game;
import tech.shadowsystems.skywars.utility.ChatUtil;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class ListCommand extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (Game game : Skywars.getInstance().getGames()) {
            sender.sendMessage(ChatUtil.format("&9Skywars &7>> &f" + game.getDisplayName() + " - " + game.getPlayers().size() + " (alive) players"));
        }
    }
}

package tech.shadowsystems.skywars.commands;

import org.bukkit.command.CommandSender;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public abstract class SubCommand {

    public abstract void execute(CommandSender sender, String[] args);

}

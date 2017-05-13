package tech.shadowsystems.skywars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.shadowsystems.skywars.Skywars;
import tech.shadowsystems.skywars.utility.ChatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class SkywarsCommand implements CommandExecutor {

    private JoinCommand joinCommand;
    private ListCommand listCommand;

    public SkywarsCommand() {
        this.joinCommand = new JoinCommand();
        this.listCommand = new ListCommand();;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.format("&9Skywars &7>> &f/skywars &f&ljoin &7- &fJoin a Skywars game"));
        } else {
            String argument = args[0];
            List<String> newArgs = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }

                newArgs.add(args[i]);
            }

            if (argument.equalsIgnoreCase("join")) {
                System.out.println("ssmode " + Skywars.getInstance().isSingleServerMode());
                if (!Skywars.getInstance().isSingleServerMode()) {
                    this.joinCommand.execute(sender, newArgs.toArray(new String[0]));
                }
            } else if (argument.equalsIgnoreCase("list")) {
                this.listCommand.execute(sender, newArgs.toArray(new String[0]));
            } else {
                sender.sendMessage(ChatUtil.format("&9Skywars &7>> &cThat command doesn't exist!"));
            }
        }

        return false;
    }

}

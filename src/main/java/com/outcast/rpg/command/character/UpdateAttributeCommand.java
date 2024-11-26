package com.outcast.rpg.command.character;

import com.outcast.rpg.command.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateAttributeCommand extends AbstractCommand {

    public UpdateAttributeCommand() {
        super("attribute");
        setDescription("Adds the given attribute and amount to the player.");
        setUsage("/attribute <add/remove> <player> <type> <amount>");
        addTabComplete(0, "add");
        addTabComplete(0, "remove");
        setPermission("rpg.attribute.add");
//        setPermissionMessage("You don't have the permission to add attributes to that player!");

        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
//                if(args[0].equals("add")) PluginLoader.getAttributeFacade().addPlayerAttribute(Bukkit.getPlayer(args[1], ,Double.parseDouble(args[3]));
//                else PluginLoader.getAttributeFacade().addPlayerAttribute(Bukkit.getPlayer(args[1], ,Double.parseDouble(args[3]));
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

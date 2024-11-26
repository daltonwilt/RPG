package com.outcast.rpg.command.character;

import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateExperienceCommand extends AbstractCommand {

    public UpdateExperienceCommand() {
        super("experience");
        setDescription("Sets the class for a player.");
        setUsage("/experience <add/remove> <player> <amount>");
        addTabComplete(0, "add");
        addTabComplete(0, "remove");
        setPermission("rpg.experience.update");
//        setPermissionMessage("You don't have the permission to add experience.");

        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                if(args[0].equals("add")) PluginLoader.getCharacterFacade().addPlayerExperience(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
                else PluginLoader.getCharacterFacade().removePlayerExperience(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

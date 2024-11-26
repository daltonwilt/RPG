package com.outcast.rpg.command.role;

import com.outcast.rpg.character.role.Role;
import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetRoleCommand extends AbstractCommand {

    public SetRoleCommand() {
        super("set");
        setDescription("Sets the class for a player.");
        setUsage("/set <player> <role>");
        setPermission("rpg.class.set");
//        setPermissionMessage("You don't have the permission to set player role.");
        for(Role role : PluginLoader.getCharacterService().getAllRoles()) {
            addTabComplete(1, role.getName());
        }

        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                PluginLoader.getCharacterFacade().setRole(sender, Bukkit.getPlayer(args[0]), args[1]);
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

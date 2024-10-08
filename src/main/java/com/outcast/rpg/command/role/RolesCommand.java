package com.outcast.rpg.command.role;

import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RolesCommand extends AbstractCommand {

    public RolesCommand() {
        super("roles");
        setDescription("Base role command. Displays list of roles.");
        setUsage("/roles");
        setPermission("rpg.roles.base");
//        setPermissionMessage("You don't have the permission to see roles.");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                PluginLoader.getCharacterFacade().displayRoles(player);
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

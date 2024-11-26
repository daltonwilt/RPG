package com.outcast.rpg.command.character;

import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CharacterCommand extends AbstractCommand {

    public CharacterCommand() {
        super("character");
        setDescription("Display information about your role.");
        setUsage("/character");
        setPermission("rpg.character.base");
//        setPermissionMessage("You don't have the permission to see your role information.");

        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                PluginLoader.getCharacterFacade().displayPlayerCharacter(player);
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

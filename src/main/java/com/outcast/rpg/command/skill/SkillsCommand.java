package com.outcast.rpg.command.skill;

import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillsCommand extends AbstractCommand {

    public SkillsCommand() {
        super("skills");
        setDescription("Displays list of unlocked skills.");
        setUsage("/skills");
        setPermission("rpg.skills.base");
//        setPermissionMessage("You don't have the permission to see skills.");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                PluginLoader.getCharacterFacade().displayAllSkills(player);
            } catch (CommandException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

package com.outcast.rpg.command.skill;

import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.command.AbstractCommand;
import com.outcast.rpg.util.setting.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillCommand extends AbstractCommand {

    public SkillCommand() {
        super("cast");
        setDescription("Casts a given skill. You must have permission to use the skill.");
        setUsage("/cast <skill>");
        setPermission("rpg.skills.cast");
//        setPermissionMessage("You don't have the permission to cast this skill.");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            try {
                Castable skill = PluginLoader.getSkillService().getAllSkills().get(args[0].toLowerCase());
                PluginLoader.getSkillFacade().playerCastSkill(player, skill, "");
            } catch (CommandException ignored) {}
        } else {
            sender.sendMessage("Player doesn't exist!");
        }

        return true;
    }

}

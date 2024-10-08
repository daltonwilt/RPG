package com.outcast.rpg.command;

import com.outcast.rpg.command.character.UpdateAttributeCommand;
import com.outcast.rpg.command.character.UpdateExperienceCommand;
import com.outcast.rpg.command.role.SetRoleCommand;
import com.outcast.rpg.command.character.CharacterCommand;
import com.outcast.rpg.command.role.RolesCommand;
import com.outcast.rpg.command.skill.SkillCommand;
import com.outcast.rpg.command.skill.SkillsCommand;

public class CommandService {

    // Create method that grabs all commands that extend AbstractCommand then instantiate them
    public static void registerCommands() {
        new UpdateAttributeCommand();

        new RolesCommand();
        new SetRoleCommand();

        new CharacterCommand();

        new UpdateExperienceCommand();

        new SkillsCommand();
        new SkillCommand();
    }

}

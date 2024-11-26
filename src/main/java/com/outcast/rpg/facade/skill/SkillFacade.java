package com.outcast.rpg.facade.skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.skill.CastErrors;
import com.outcast.rpg.api.skill.CastResult;
import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.service.skill.SkillService;
import com.outcast.rpg.util.exception.CastException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Singleton
public class SkillFacade {

    @Inject SkillService skillService;

    SkillFacade() {}

    public void playerCastSkill(Player user, Castable skill, String... args) throws CastException {
        livingCastSkill(user, skill, args);
    }

    public void livingCastSkill(LivingEntity user, Castable skill, String... args) throws CastException  {
        if(skill == null)
            throw CastErrors.exceptionOf(user, Component.text("Must provide valid skill"));

        if(args == null)
            args = new String[0];

        CastResult result = skillService.cast(user, skill, System.currentTimeMillis(), args);

        if(result == null) {
            throw CastErrors.internalError(user);
        } else {
            TextComponent message = result.getMessage();

            if(!(message == Component.empty()))
                user.sendMessage(message);
        }
    }

}

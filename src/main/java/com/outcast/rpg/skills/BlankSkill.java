package com.outcast.rpg.skills;

import com.outcast.rpg.api.skill.CastResult;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.api.character.skill.SkillSpec;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;

public final class BlankSkill extends Skill {
    public static final BlankSkill blank = new BlankSkill("blank", "blank");

    public BlankSkill(String id, String name) {
        super(SkillSpec.create()
                .id(id)
                .name(name)
                .cooldown("0")
                .resourceCost("0")
                .description(Component.empty())
        );
    }

    @Override
    public CastResult cast(LivingEntity user, long timestamp, String... args) {
        return null;
    }
}

package com.outcast.rpg.api.character.skill;

import com.outcast.rpg.api.skill.TargetedCastable;
import org.bukkit.entity.LivingEntity;

public abstract class TargetedSkill extends Skill implements TargetedCastable {

    public static final String MAX_RANGE_PROPERTY = "max-range";

    protected TargetedSkill(SkillSpec skillSpec) { super(skillSpec); }

    @Override
    public double getRange(LivingEntity user) {
        return asDouble(user, getProperty(MAX_RANGE_PROPERTY, String.class, "10.0"));
    }
}

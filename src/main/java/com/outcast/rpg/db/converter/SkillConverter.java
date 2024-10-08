package com.outcast.rpg.db.converter;

import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.skills.BlankSkill;
import com.outcast.rpg.util.setting.PluginLoader;
import jakarta.persistence.AttributeConverter;

public class SkillConverter implements AttributeConverter<Skill, String> {

    @Override
    public String convertToDatabaseColumn(Skill skill) {
        return skill.getId();
    }

    @Override
    public Skill convertToEntityAttribute(String skillId) {
        return (Skill) PluginLoader.getSkillService().getSkillById(skillId).orElse(BlankSkill.blank);
    }

}

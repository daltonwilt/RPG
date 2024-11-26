package com.outcast.rpg.api.skill;

import com.outcast.rpg.util.exception.CastException;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public interface Castable {

    String getId();
    String getName();
    String getPermission();
    TextComponent getDescription(LivingEntity user);
    long getCooldown(LivingEntity user);
    double getResource(LivingEntity user);

    /**
     * Triggers the use of this Castable by the provided living entity
     *
     * @param user              The use of the skill
     * @param timestamp         The timestamp of when the skill is triggered
     * @param args              The arguments of the skill being used
     * @return                  A {@link CastResult}
     * @throws CastException    If a skill-related error occurs
     */
    CastResult cast(LivingEntity user, long timestamp, String... args) throws CastException;

}

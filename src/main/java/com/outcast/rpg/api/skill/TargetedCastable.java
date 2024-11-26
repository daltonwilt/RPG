package com.outcast.rpg.api.skill;

import com.outcast.rpg.util.exception.CastException;
import com.outcast.rpg.util.physics.Physics;
import org.bukkit.entity.LivingEntity;

import java.util.Set;

public interface TargetedCastable extends Castable {

    @Override
    default CastResult cast(LivingEntity user, long timestamp, String... args) throws CastException {
        double range = getRange(user);

        Set<LivingEntity> entities = Physics.raycastEntities(user.getLocation(), user.getLocation().getDirection().normalize(), range);

        if(!entities.isEmpty())
            return cast(user, entities.iterator().next(), timestamp, args);

        throw CastErrors.noTarget(user);
    }

    CastResult cast(LivingEntity user, LivingEntity target, long timestamp, String... args) throws CastException;

    default double getRange(LivingEntity user) {
        return 10.0;
    }
}

package com.outcast.rpg.service.skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.api.character.ResourceUser;
import com.outcast.rpg.api.event.SkillCastEvent;
import com.outcast.rpg.api.skill.CastErrors;
import com.outcast.rpg.api.skill.CastResult;
import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.service.character.ResourceService;
import com.outcast.rpg.util.exception.CastException;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class SkillService {

    private final Map<String, Castable> skills = new HashMap<>();

    @Inject ResourceService resourceService;
    @Inject CooldownService cooldownService;

    SkillService() {}

    public void registerSkill(Castable castable) {
        skills.put(castable.getName().toLowerCase(), castable);
        skills.put(castable.getId(), castable);
    }
    public void registerSkills(Castable... castables) {
        for (Castable castable : castables) {
            registerSkill(castable);
        }
    }

    public Optional<Castable> getSkillById(String id) {
        RPG.warn(id);
        for(String key : skills.keySet())
            RPG.warn(key);
        return Optional.ofNullable(skills.get(id));
    }
    public Map<String, Castable> getAllSkills() {
        return skills;
    }

    /**
     * Casts a castable with the properties stored within this carrier
     *
     * @param user      The caster casting the skill
     * @param castable  the castable skill
     * @param timestamp When the skill is being cast
     * @param args      arguments
     * @return a {@link CastResult}
     */
    public CastResult cast(LivingEntity user, Castable castable, long timestamp, String... args) throws CastException {
        // Trigger the pre-cast event
        SkillCastEvent.Pre preCastEvent = new SkillCastEvent.Pre(user, castable, timestamp);
        Bukkit.getPluginManager().callEvent(preCastEvent);

        // If the pre-cast event was cancelled, throw a cancelled exception
        if(preCastEvent.isCancelled())
            throw CastErrors.cancelled(user, castable);

        // Set the user, skill and skill properties to what was set in the pre-cast event
        user = preCastEvent.getUser();
        castable = preCastEvent.getSkill();

        // Validate
        if(validate(user, castable, timestamp)) {
            // Cast the skill
            CastResult result = castable.cast(user, timestamp, args);

            // Set cooldown(s) and withdraw resources
            cooldownService.setGlobalCooldown(user, timestamp);
            cooldownService.setLastUsedTimestamp(user, castable, timestamp);
            resourceService.withdrawResource(user, castable.getResource(user));

            // Trigger the post-cast event with the result
            Bukkit.getPluginManager().callEvent(new SkillCastEvent.Post(user, castable, timestamp, result));

            return result;
        } else {
            throw CastErrors.internalError(user);
        }
    }

    private boolean validate( LivingEntity user, Castable castable, long timestamp) throws CastException {
        boolean valid = validatePermission(user, castable);
        valid = valid && validateGlobalCooldown(user, timestamp);
        valid = valid && validateCooldown(user, castable, timestamp);
        valid = valid && validateResources(user, castable);

        return valid;
    }

    private boolean validatePermission(LivingEntity user, Castable skill) throws CastException {
        // If the user is a player, check for permission.
        // If the user is not a player, just return true ( is presumed to be non-player character )
        if (user instanceof Player player) {
            String permission = skill.getPermission();

            // If no permission is set, just return true
            if (permission == null)
                return true;

            boolean permitted = player.hasPermission(permission);

            if (!permitted)
                throw CastErrors.noPermission(user, skill);
        }

        return true;
    }

    private boolean validateGlobalCooldown(LivingEntity user, Long timestamp) throws CastException {
        if (cooldownService.isOnGlobalCooldown(user, timestamp)) {
            long cooldownEnd = cooldownService.getLastGlobalCooldownEnd(user);
            throw CastErrors.onGlobalCooldown(user, timestamp, cooldownEnd);
        }

        return true;
    }

    private boolean validateCooldown(LivingEntity user, Castable castable, Long timestamp) throws CastException {
        long lastUsed = cooldownService.getLastUsedTimestamp(user, castable);
        long cooldownDuration = castable.getCooldown(user);

        if (cooldownService.isCooldownOngoing(timestamp, lastUsed, cooldownDuration)) {
            long cooldownEnd = cooldownService.getCooldownEnd(lastUsed, cooldownDuration);
            throw CastErrors.onCooldown(user, timestamp, castable, cooldownEnd);
        }

        return true;
    }

    private boolean validateResources(LivingEntity user, Castable castable) throws CastException {
        ResourceUser resourceUser = resourceService.getOrCreateUser(user);

        if(resourceUser.getCurrent() < castable.getResource(user))
            throw CastErrors.insufficientResources(user, castable);

        return true;
    }

}

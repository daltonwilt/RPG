package com.outcast.rpg.service.skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.config.RPGSkillsConfig;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class CooldownService {

    @Inject RPGSkillsConfig rpgSkillsConfig;

    private final Map<UUID, Long> globalCooldowns = new HashMap<>();
    private final Map<UUID, Map<Castable, Long>> lastUsed = new HashMap<>();

    CooldownService() {}

    /**
     * Get when the provided user's last global cooldown began
     *
     * @param user The user to check
     * @return When the user's last global cooldown began, or 0L if the player has never been put on global cooldown
     */
    public long getLastGlobalCooldownStart(LivingEntity user) {
        return globalCooldowns.getOrDefault(user.getUniqueId(), 0L);
    }

    /**
     * Get when ( in unix timestamp form ) the provided user's last global cooldown is/was due to end
     *
     * @param user The user to check
     * @return The cooldown end
     */
    public long getLastGlobalCooldownEnd(LivingEntity user) {
        return getLastGlobalCooldownStart(user) + rpgSkillsConfig.GLOBAL_COOLDOWN;
    }

    /**
     * Put the user on a global cooldown
     *
     * @param user      The user to put on cooldown
     * @param timestamp The current timestamp
     */
    public void setGlobalCooldown(LivingEntity user, long timestamp) {
        globalCooldowns.put(user.getUniqueId(), timestamp);
    }

    /**
     * Retrieve the last time a user used the provided skill
     *
     * @param user     The user to check
     * @param castable The skill to check
     * @return The timestamp, or 0L if never used before
     */
    public long getLastUsedTimestamp(LivingEntity user, Castable castable) {
        Map<Castable, Long> lastUsedSkills = lastUsed.get(user.getUniqueId());

        if (lastUsedSkills == null) {
            return 0L;
        } else {
            return lastUsedSkills.getOrDefault(castable, 0L);
        }
    }

    /**
     * Set the timestamp for when the last time was that the provided user cast the skill
     *
     * @param user      The user who cast the skill
     * @param castable  The skill to put on cooldown
     * @param timestamp The current timestamp
     */
    public void setLastUsedTimestamp(LivingEntity user, Castable castable, long timestamp) {
        Map<Castable, Long> userSkillCooldowns;

        if (lastUsed.containsKey(user.getUniqueId())) {
            userSkillCooldowns = lastUsed.get(user.getUniqueId());
        } else {
            userSkillCooldowns = new HashMap<>();
        }

        userSkillCooldowns.put(castable, timestamp);
        lastUsed.put(user.getUniqueId(), userSkillCooldowns);
    }

    /**
     * Check if the user is on a global cooldown
     *
     * @param user The user to check
     * @return Whether the user is on a global cooldown
     */
    public boolean isOnGlobalCooldown(LivingEntity user, long currentTimestamp) {
        return isCooldownOngoing(currentTimestamp, getLastGlobalCooldownStart(user), rpgSkillsConfig.GLOBAL_COOLDOWN);
    }

    public long getCooldownEnd(long cooldownStart, long duration) {
        return cooldownStart + duration;
    }

    public boolean isCooldownOngoing(long currentTimestamp, long cooldownStart, long cooldownDuration) {
        return cooldownStart > 0L && currentTimestamp < (cooldownStart + cooldownDuration);
    }

}

package com.outcast.rpg.service.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.api.character.ResourceUser;
import com.outcast.rpg.api.event.ResourceEvent;
import com.outcast.rpg.character.EntityResourceUser;
import com.outcast.rpg.config.RPGSkillsConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ResourceService {

    @Inject private RPGSkillsConfig skillsConfig;
    private final Map<UUID, ResourceUser> resourceUsers = new HashMap<>();

    public void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                regenResources();
            }
        }.runTaskTimer(RPG.getInstance(), 0, skillsConfig.RESOURCE_REGEN_TICK_INTERVAL);
    }

    public void regenResources() {
        resourceUsers.forEach((uuid, user) -> {
            if (user.getCurrent() >= user.getMax()) {
                return;
            }

            double regenAmount = skillsConfig.RESOURCE_REGEN_RATE;

            if (user.getMax() - user.getCurrent() <= regenAmount) {
                regenAmount = user.getMax() - user.getCurrent();
            }

            // TODO: Make this work for all living entities, not just players
            Player entity = Bukkit.getServer().getPlayer(uuid);

            ResourceEvent.Regen event;
            if (entity != null) {
                event = new ResourceEvent.Regen(entity, user, regenAmount);
            } else {
                event = new ResourceEvent.Regen(user, regenAmount);
            }

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            user.fill(event.getRegenAmount());
        });
    }

    public ResourceUser getOrCreateUser(LivingEntity user) {
        ResourceUser resourceUser = resourceUsers.get(user.getUniqueId());

        if (resourceUser == null) {
            resourceUser = new EntityResourceUser(user);
            resourceUser.setMax(skillsConfig.RESOURCE_LIMIT);
            resourceUser.fill();
            resourceUsers.put(user.getUniqueId(), resourceUser);

            Bukkit.getPluginManager().callEvent(new ResourceEvent.Create(user, resourceUser));
        }
        return resourceUser;
    }

    public void withdrawResource(LivingEntity user, double amount) {
        getOrCreateUser(user).drain(amount);
    }

}

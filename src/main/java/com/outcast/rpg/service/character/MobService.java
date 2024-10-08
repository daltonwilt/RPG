package com.outcast.rpg.service.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.config.mob.MobConfig;
import com.outcast.rpg.config.mob.MobsConfig;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.*;

import static org.bukkit.ChatColor.*;

@Singleton
public class MobService {

    @Inject
    private AttributeService attributeService;
    @Inject
    private CharacterService characterService;

    @Inject
    private MobsConfig mobsConfig;

    private final Map<String, LivingEntity> mobsCache = new HashMap<>();

//    Maybe use this to preload the mobs into objects first on init
//    public void init(LivingEntity living) {
//        mobsConfig.MOBS.forEach((id, mobConfig) -> {
//        });
//    }

    public void registerMob(String id, LivingEntity living) {
        MobConfig mobConfig = mobsConfig.MOBS.get(id);
        if(mobConfig != null) {
            Map<AttributeType, Double> attributes = new HashMap<>(mobConfig.DEFAULT_ATTRIBUTES);
            assignEntityAttributes(living, attributes, mobConfig.DAMAGE_EXPRESSION);
        }
    }

    public Optional<LivingEntity> getMob(String id) {
        return Optional.ofNullable(mobsCache.get(id));
    }

    public void assignEntityAttributes(LivingEntity living, Map<AttributeType, Double> mobAttributes, String damageExpression) {
        Map<AttributeType, Double> attributes = attributeService.fillAttributes(mobAttributes);
        String name = living.getName().toLowerCase().replace("_", " ");

        for(AttributeType type : attributes.keySet()) {
            if(type.getId().equals("rpg:max_health")) {
                living.setCustomName(RED + name + DARK_GRAY +" [ "+ GOLD  + attributes.get(type) + DARK_GRAY  +" ]");
                living.setCustomNameVisible(true);
                living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(attributes.get(type));
                living.setHealth(attributes.get(type));
            } else if(type.getId().equals("rpg:base_damage")) {
//                living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(attributes.get(type));
            }

        }

        mobsCache.put(living.getUniqueId().toString(), living);
    }

}

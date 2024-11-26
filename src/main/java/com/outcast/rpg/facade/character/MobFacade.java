package com.outcast.rpg.facade.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.loot.ExperienceLootConfig;
import com.outcast.rpg.config.loot.LootConfig;
import com.outcast.rpg.config.mob.MobConfig;
import com.outcast.rpg.config.mob.MobsConfig;
import com.outcast.rpg.facade.character.CharacterFacade;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.service.character.CharacterService;
import com.outcast.rpg.service.character.MobService;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.text.DecimalFormat;
import java.util.*;

@Singleton
public class MobFacade {

    @Inject
    private RPGConfig rpgConfig;
    @Inject
    private MobsConfig mobsConfig;

    @Inject
    private AttributeService attributeService;
    @Inject
    private CharacterService characterService;
    @Inject
    private MobService mobService;

    @Inject
    private CharacterFacade characterFacade;

    private DecimalFormat decimalFormat = new DecimalFormat();
    private static Random random = new Random();

    public void dropMobLoot(LivingEntity mob, Player killer) {
        Optional<MobConfig> lootOptional = getMobConfig(mob);

        if(lootOptional.isPresent()) {
            int itemLimit = lootOptional.get().ITEM_DROP_LIMIT;
            Set<LootConfig> lootConfigs = lootOptional.get().LOOT;

            if(lootConfigs.isEmpty()) return;

            Set<Player> playersToReceiveLoot = new HashSet<>();
            playersToReceiveLoot.add(killer);

            // Eventually create party splitting mechanic

            int playersToReceiveLootSize = playersToReceiveLoot.size();

            for(LootConfig lootConfig : lootConfigs) {
                double drop = random.nextDouble();

                // Roll for drop chance is unsuccessful and this loot item will not drop
                if(drop >= lootConfig.DROP_RATE)
                    continue;

//                if(lootConfig.CURRENCY != null) playersToReceiveLoot.forEach();

                if(lootConfig.EXPERIENCE != null)
                    playersToReceiveLoot.forEach(player -> awardPlayerExperienceLoot(player, lootConfig.EXPERIENCE, playersToReceiveLootSize));

//                if(lootConfig.ITEM != null && itemLimit != 0) {
//                    dropEquipmentLoot(mob.getLocation(), lootConfig.ITEM);
//                    itemLimit--;
//                }
            }

        }
    }

    private void awardPlayerExperienceLoot(Player player, ExperienceLootConfig config, int split) {
        characterFacade.addPlayerExperience(
                player,
                ( Math.floor( RandomUtils.nextDouble(config.MINIMUM, config.MAXIMUM) * 100) / 100 )
        );
    }

    public void onMobSpawn(EntitySpawnEvent event) {
        if((event.getEntity() instanceof LivingEntity living) && !(event.getEntity() instanceof Player)) {
            String id = living.getName().toLowerCase().replace("_", " ");
            mobService.registerMob(id, living);
        }
    }

    public Optional<MobConfig> getMobConfig(LivingEntity living) {
        String name = living.getType().name().toLowerCase();
        return Optional.ofNullable(mobsConfig.MOBS.get(name));
    }

}

package com.outcast.rpg.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.facade.character.CharacterFacade;
import com.outcast.rpg.facade.character.MobFacade;
import com.outcast.rpg.facade.character.ResourceFacade;
import com.outcast.rpg.service.character.CharacterService;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DecimalFormat;

import static org.bukkit.ChatColor.*;

@Singleton
public class EntityListener implements Listener {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Inject
    private RPGConfig rpgConfig;

    @Inject
    private CharacterFacade characterFacade;
    @Inject
    private ResourceFacade resourceFacade;
    @Inject
    private MobFacade mobFacade;

    @Inject
    private CharacterService characterService;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        mobFacade.onMobSpawn(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if((event.getEntity() instanceof LivingEntity living) && !(event.getEntity() instanceof Player)) {
            String amount;
            if((living.getHealth() - event.getDamage()) < 0)
                amount = "0";
            else
                amount = df.format(living.getHealth() - event.getDamage());

            living.setCustomName(DARK_GRAY + "[ " + GOLD + amount + DARK_GRAY + " ]");
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            if(event.getEntity().getKiller() != null)
                mobFacade.dropMobLoot(event.getEntity(), event.getEntity().getKiller());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        resourceFacade.onPlayerJoin(event.getPlayer());
    }

}

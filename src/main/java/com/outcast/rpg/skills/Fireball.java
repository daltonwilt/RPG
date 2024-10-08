package com.outcast.rpg.skills;

import com.outcast.rpg.api.skill.CastResult;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.api.character.skill.SkillSpec;
import net.kyori.adventure.text.Component;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.javatuples.Pair;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Fireball extends Skill implements Listener {
    private static final String AOE_RANGE = "aoe-range";
    private static final String AOE_DAMAGE_MODIFIER = "aoe-damage-modifier";

    private static final String DEFAULT_DAMAGE_EXPRESSION = "5.0";

    private final Map<UUID, LivingEntity> icicles = new WeakHashMap<>();

    public Fireball() {
        super(
                SkillSpec.create()
                        .id("fireball")
                        .name("Fireball")
                        .description(Component.text("Drop an Fireball onto your target."))
                        .cooldown("2000")
                        .resourceCost("15")
        );

        setDescriptionArgs(new Pair[]{new Pair<>("damage", 2)});
    }

    private void damage(Entity target, LivingEntity user) {
        if(target instanceof LivingEntity leTarget && !target.equals(user)) {
            double damage = (asDouble(user, (LivingEntity) target, getProperty("damage", String.class, DEFAULT_DAMAGE_EXPRESSION)));
            leTarget.damage(damage, user);
        }
    }

    @Override
    public CastResult cast(LivingEntity user, long timestamp, String... args) {
        Snowball fireball = user.launchProjectile(Snowball.class);
        fireball.setVelocity(fireball.getVelocity().normalize().multiply(1.5D));
        fireball.setFireTicks(100); // 5 seconds
        fireball.setShooter(user);
        user.getWorld().playEffect(user.getLocation(), Effect.BLAZE_SHOOT, 1);
        return CastResult.success();
    }

    @EventHandler
    public void collide(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Snowball  && event.getHitEntity() instanceof LivingEntity target) {
            damage(event.getHitEntity(), (LivingEntity) event.getEntity().getShooter());
        }
    }

}

package com.outcast.rpg.config;

import com.google.inject.Singleton;
import com.outcast.rpg.db.JPAConfig;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class RPGConfig extends PluginConfig {

    @Setting("equipment-damage-types")
    public Map<Material, String> EQUIPMENT_DAMAGE_TYPES = new HashMap<>();

    @Setting("offhand-equipment-types")
    public Set<Material> OFFHAND_EQUIPMENTS = new HashSet<>();

    @Setting("mainhand-equipment-types")
    public Set<Material> MAINHAND_EQUIPMENTS = new HashSet<>();

    @Setting("projectile-damage-types")
    public Map<EntityType, String> PROJECTILE_DAMAGE_TYPES = new HashMap<>();

    @Setting("physical-damage-mitigation-calculation")
    public String PHYSICAL_DAMAGE_MITIGATION_CALCULATION = "1.33 * SOURCE_CON";

    @Setting("magical-damage-mitigation-calculation")
    public String MAGICAL_DAMAGE_MITIGATION_CALCULATION = "1.33 * SOURCE_INT";

    @Setting("damage-calculations")
    public Map<String, String> DAMAGE_CALCULATIONS = new HashMap<>();

    @Setting("environmental-damage-calculations")
    public Map<EntityDamageEvent.DamageCause, String> ENVIRONMENTAL_CALCULATIONS = new HashMap<>();

    @Setting("default-melee-damage-type")
    public String DEFAULT_MELEE_TYPE = "unarmed";

    @Setting("default-ranged-damage-type")
    public String DEFAULT_RANGED_TYPE = "ranged";

    @Setting("health-regen-duration-in-ticks")
    public long HEALTH_REGEN_DURATION_TICKS = 1;

    @Setting("health-regen-calculation")
    public String HEALTH_REGEN_CALCULATION = "1.33 * SOURCE_CON";

    @Setting("resource-regen-calculation")
    public String RESOURCE_REGEN_CALCULATION = "1.33 * SOURCE_INT";

    @Setting("resource-limit-calculation")
    public String RESOURCE_LIMIT_CALCULATION = "100.0 + SOURCE_INT * 1.5";

    @Setting("health-limit-calculation")
    public String HEALTH_LIMIT_CALCULATION = "100.0 + SOURCE_CON * 1.5";

    @Setting("health-scaling")
    public double HEALTH_SCALING = 20d;

    @Setting("movement-speed-calculation")
    public String MOVEMENT_SPEED_CALCULATION = "0.1";

    @Setting("attribute-upgrade-cost")
    public String ATTRIBUTE_UPGRADE_COST = "100.0";

    @Setting("experience-max")
    public double EXPERIENCE_MAX = 100_000.0;

    @Setting("experience-min")
    public double EXPERIENCE_MIN = 0.0;

    @Setting("attribute-max")
    public double ATTRIBUTE_MAX = 99.0;

    @Setting("attribute-min")
    public double ATTRIBUTE_MIN = 0.0;

//    @Setting("experience-spending-limit")
//    public double EXPERIENCE_SPENDING_LIMIT = 100_000.0;
//
//    @Setting("attribute-spending-limit")
//    public double ATTRIBUTE_SPENDING_LIMIT = 100_000.0;
//
//    @Setting("skill-spending-limit")
//    public double SKILL_SPENDING_LIMIT = 100_000.0;

    @Setting("display-root-skill")
    public boolean DISPLAY_ROOT_SKILL = true;

    @Setting("skill-message-distance")
    public double SKILL_MESSAGE_DISTANCE = 25;

    @Setting("max-reward-distance")
    public double MAX_REWARD_DISTANCE = 30.0d;

    {
        // Wood
        EQUIPMENT_DAMAGE_TYPES.put(Material.WOODEN_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.WOODEN_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.WOODEN_PICKAXE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.WOODEN_AXE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.WOODEN_SWORD, "blunt");

        // Stone
        EQUIPMENT_DAMAGE_TYPES.put(Material.STONE_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.STONE_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.STONE_PICKAXE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.STONE_AXE, "slash");
        EQUIPMENT_DAMAGE_TYPES.put(Material.STONE_SWORD, "slash");

        // Iron
        EQUIPMENT_DAMAGE_TYPES.put(Material.IRON_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.IRON_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.IRON_PICKAXE, "stab");
        EQUIPMENT_DAMAGE_TYPES.put(Material.IRON_AXE, "slash");
        EQUIPMENT_DAMAGE_TYPES.put(Material.IRON_SWORD, "stab");

        // Gold
        EQUIPMENT_DAMAGE_TYPES.put(Material.GOLDEN_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.GOLDEN_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.GOLDEN_PICKAXE, "stab");
        EQUIPMENT_DAMAGE_TYPES.put(Material.GOLDEN_AXE, "slash");
        EQUIPMENT_DAMAGE_TYPES.put(Material.GOLDEN_SWORD, "stab");

        // Diamond
        EQUIPMENT_DAMAGE_TYPES.put(Material.DIAMOND_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.DIAMOND_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.DIAMOND_PICKAXE, "stab");
        EQUIPMENT_DAMAGE_TYPES.put(Material.DIAMOND_AXE, "slash");
        EQUIPMENT_DAMAGE_TYPES.put(Material.DIAMOND_SWORD, "stab");

        // Netherite
        EQUIPMENT_DAMAGE_TYPES.put(Material.NETHERITE_HOE, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.NETHERITE_SHOVEL, "blunt");
        EQUIPMENT_DAMAGE_TYPES.put(Material.NETHERITE_PICKAXE, "stab");
        EQUIPMENT_DAMAGE_TYPES.put(Material.NETHERITE_AXE, "slash");
        EQUIPMENT_DAMAGE_TYPES.put(Material.NETHERITE_SWORD, "stab");

        // Hand
        EQUIPMENT_DAMAGE_TYPES.put(Material.AIR, "unarmed");
    }

    {
        // Bow
        PROJECTILE_DAMAGE_TYPES.put(EntityType.ARROW, "ranged");
        PROJECTILE_DAMAGE_TYPES.put(EntityType.EGG, "ranged");
        PROJECTILE_DAMAGE_TYPES.put(EntityType.FIREWORK, "ranged");
        PROJECTILE_DAMAGE_TYPES.put(EntityType.TRIDENT, "ranged");
        PROJECTILE_DAMAGE_TYPES.put(EntityType.SNOWBALL, "ranged");
        PROJECTILE_DAMAGE_TYPES.put(EntityType.SPECTRAL_ARROW, "ranged");
    }

    {
        DAMAGE_CALCULATIONS.put("blunt", "CLAMP(SOURCE_CON, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("stab", "CLAMP(SOURCE_STR, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("slash", "CLAMP(SOURCE_STR, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("unarmed", "CLAMP(SOURCE_INT, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("ranged", "CLAMP(SOURCE_DEX, 1.0, 15.0)");
    }

    public RPGConfig() throws IOException {
        super("plugins/rpg", "rpg-config.conf");
    }

}

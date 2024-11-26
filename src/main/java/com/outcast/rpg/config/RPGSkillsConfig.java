package com.outcast.rpg.config;

import com.outcast.rpg.util.setting.PluginConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import ninja.leaping.configurate.objectmapping.Setting;
import org.bukkit.Material;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RPGSkillsConfig extends PluginConfig {

    @Setting("global-cooldown")
    public long GLOBAL_COOLDOWN = 500;

    @Setting("resource-regen-interval-ticks")
    public int RESOURCE_REGEN_TICK_INTERVAL = 20;

    @Setting("resource-regen-rate")
    public double RESOURCE_REGEN_RATE = 5;

    @Setting("resource-limit")
    public double RESOURCE_LIMIT = 100.0d;

    @Setting("resource-name")
    public String RESOURCE_NAME = "Mana";

    @Setting("resource-colour")
    public NamedTextColor RESOURCE_COLOR = NamedTextColor.BLUE;

    @Setting("passable-blocks")
    public Set<Material> PASSABLE_BLOCKS = new HashSet<>();
    {
        PASSABLE_BLOCKS.addAll(Arrays.asList(
                Material.AIR, Material.TALL_GRASS, Material.POWDER_SNOW, Material.WATER, Material.GRASS, Material.WHEAT, Material.SUGAR_CANE ,
                Material.VINE, Material.PITCHER_PLANT, Material.CHORUS_PLANT, Material.CAVE_VINES_PLANT, Material.KELP_PLANT,
                Material.TWISTING_VINES_PLANT, Material.WEEPING_VINES_PLANT, Material.COCOA
        ));
    }

    protected RPGSkillsConfig() throws IOException {
        super("plugins/rpg", "skills-config.conf");
        init();
    }

}

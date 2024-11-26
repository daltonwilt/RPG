package com.outcast.rpg.config.attribute;

import com.outcast.rpg.util.setting.PluginConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class AttributesConfig extends PluginConfig  {

    @Setting("attribute-types")
    public List<AttributeTypeConfig> ATTRIBUTE_TYPES = new ArrayList<>();
    {
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:strength",
                "str",
                "Strength",
                "",
                true,
                false,
                NamedTextColor.RED,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:constitution",
                "con",
                "Constitution",
                "",
                true,
                false,
                NamedTextColor.YELLOW,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:dexterity",
                "dex",
                "Dexterity",
                "",
                true,
                false,
                NamedTextColor.GREEN,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:intelligence",
                "int",
                "Intelligence",
                "",
                true,
                false,
                NamedTextColor.BLUE,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:wisdom",
                "wis",
                "Wisdom",
                "",
                true,
                false,
                NamedTextColor.LIGHT_PURPLE,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:magical_resistance",
                "magicres",
                "Magical Resistance",
                "",
                false,
                false,
                NamedTextColor.AQUA,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:physical_resistance",
                "physres",
                "Physical Resistance",
                "",
                false,
                false,
                NamedTextColor.GOLD,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:base_armor",
                "armor",
                "Base Armor",
                "",
                false,
                true,
                NamedTextColor.DARK_AQUA,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:base_damage",
                "dmg",
                "Base Damage",
                "",
                false,
                true,
                NamedTextColor.DARK_RED,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:max_health",
                "health",
                "Maximum Health",
                "",
                false,
                true,
                NamedTextColor.WHITE,
                0.0d,
                true,
                ""
        ));

    }

    public AttributesConfig() throws IOException {
        super("plugins/rpg", "attributes.conf");
    }

}

package com.outcast.rpg.config.loot;

import com.outcast.rpg.character.attribute.AttributeType;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class EquipmentConfig {

    @Setting("id")
    public String ID = "unique-item-id";

    @Setting("name")
    public String ITEM_NAME = "&oDefault Item Name";

    @Setting("hide-flags")
    public boolean HIDE_FLAGS = false;

    @Setting("type")
    public Material ITEM_TYPE = Material.DIRT;

    @Setting("durability")
    public int DURABILITY = -1; // if durability is -1, that means full default item durability

    @Setting("lore")
    public List<String> LORE = new ArrayList<>();

    @Setting("template")
    public String TEMPLATE = "";

    @Setting("category")
    public String CATEGORY = "";

    @Setting("rarity")
    public String RARITY = "";

    @Setting("description")
    public String DESCRIPTION = "";

    @Setting("enchantments")
    public Map<Enchantment, Integer> ENCHANTMENTS = new HashMap<>();

    @Setting("attributes")
    public Map<AttributeType, Double> ATTRIBUTES = new HashMap<>();

    @Setting("group")
    public String GROUP = "default";

}
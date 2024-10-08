package com.outcast.rpg.config.mob;

import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.config.loot.LootConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ConfigSerializable
public class MobConfig {

    // Error here because AttributeType is not serializable by gson
    @Setting("default-attributes")
    public Map<AttributeType, Double> DEFAULT_ATTRIBUTES = new HashMap<>();

    @Setting("damage-expression")
    public String DAMAGE_EXPRESSION = "5.0";

    @Setting("item-drop-limit")
    public int ITEM_DROP_LIMIT = 2;

    @Setting("entity-type")
    public EntityType ENTITY_TYPE = EntityType.ZOMBIE;

    @Setting("loot")
    public Set<LootConfig> LOOT = new HashSet<>();

}

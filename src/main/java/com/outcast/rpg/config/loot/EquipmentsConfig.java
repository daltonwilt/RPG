package com.outcast.rpg.config.loot;

import com.google.inject.Singleton;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class EquipmentsConfig extends PluginConfig {

    @Setting("items")
    public Set<EquipmentConfig> ITEMS = new HashSet<>(); { ITEMS.add(new EquipmentConfig()); }

    public EquipmentsConfig(String path) throws IOException {
        super("plugins/items/", path);
    }

}

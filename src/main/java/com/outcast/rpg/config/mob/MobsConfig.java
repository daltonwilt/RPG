package com.outcast.rpg.config.mob;

import com.google.inject.Singleton;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MobsConfig extends PluginConfig {

    @Setting("mobs")
    public Map<String, MobConfig> MOBS = new HashMap<>(); { MOBS.put("cow", new MobConfig()); }

    public MobsConfig() throws IOException {
        super("plugins/rpg", "mobs.conf");
    }

}

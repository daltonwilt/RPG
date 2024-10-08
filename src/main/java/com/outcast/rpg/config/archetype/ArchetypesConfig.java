package com.outcast.rpg.config.archetype;

import com.google.inject.Singleton;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class ArchetypesConfig extends PluginConfig {

    @Setting("archetypes")
    public Set<ArchetypeConfig> ARCHETYPES = new HashSet<>(); { ARCHETYPES.add(new ArchetypeConfig()); }

    @Setting("roles")
    public Set<RoleConfig> ROLE_CONFIGS = new HashSet<>();

    @Setting("default-archetype")
    public String DEFAULT = "None";

    public ArchetypesConfig() throws IOException {
        super("plugins/rpg", "archetypes.conf");
    }

}

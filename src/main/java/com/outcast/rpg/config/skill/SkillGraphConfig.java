package com.outcast.rpg.config.skill;

import com.google.inject.Singleton;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.util.*;

@Singleton
public class SkillGraphConfig extends PluginConfig {

    @Setting("root-skill")
    public SkillNodeConfig ROOT = new SkillNodeConfig();

    @Setting("skill-nodes")
    public Map<String, SkillNodeConfig> NODES = new HashMap<>();

    @Setting("unique-skills")
    public Set<String> UNIQUE_SKILLS = new HashSet<>();

    @Setting("skill-links")
    public List<SkillNodeLinkConfig> LINKS = new ArrayList<>();

    public SkillGraphConfig() throws IOException {
        super("plugins/rpg", "skill-graph.conf");
    }

}

package com.outcast.rpg.api.character.skill;

import net.kyori.adventure.text.TextComponent;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class SkillSpec {

    private String id;
    private String name;
    private String permission;

    private TextComponent description;
    private Pair<String, ?>[] descriptionArgs;

    private String cooldownExpression;
    private String resourceExpression;

    private Map<String, String> properties = new HashMap<>();

    protected SkillSpec() {}

    public static SkillSpec create() {
        return new SkillSpec();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }

    public TextComponent getDescription() {
        return description;
    }
    public Pair<String, ?>[] getDescriptionArgs() {
        return descriptionArgs;
    }

    public String getCooldownExpression() {
        return cooldownExpression;
    }
    public String getResourceExpression() {
        return resourceExpression;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public SkillSpec id(String id) {
        this.id = id;
        permission = "rpg.skills." + id;
        return this;
    }

    public SkillSpec name(String name) {
        this.name = name;
        return this;
    }

    public SkillSpec description(TextComponent description) {
        this.description = description;
        return this;
    }

    @SafeVarargs
    public final SkillSpec descriptionArgs(Pair<String, ?>... descriptionArgs) {
        this.descriptionArgs = descriptionArgs;
        return this;
    }

    public SkillSpec cooldown(String cooldownExpression) {
        this.cooldownExpression = cooldownExpression;
        return this;
    }

    public SkillSpec resourceCost(String resourceExpression) {
        this.resourceExpression = resourceExpression;
        return this;
    }

    public SkillSpec properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public SkillSpec property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

}

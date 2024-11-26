package com.outcast.rpg.api.skill;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public abstract class AbstractSkill implements Castable {

    private final String id;
    private final String name;

    protected AbstractSkill (String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPermission() {
        return "rpg.skills." + id;
    }

    @Override
    public TextComponent getDescription(LivingEntity user) {
        return null;
    }

}

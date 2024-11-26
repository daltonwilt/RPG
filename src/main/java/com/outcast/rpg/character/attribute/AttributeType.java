package com.outcast.rpg.character.attribute;

import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;

import java.util.Objects;

public class AttributeType {

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String shortName;
    @Getter
    private final boolean upgradable;
    @Getter
    private final boolean hidden;
    @Getter
    private final TextColor color;
    @Getter
    private final String description;
    @Getter
    private final double defaultValue;
    @Getter
    private final boolean resetOnLogin;
    private final String display;

    public AttributeType(String id, String shortName, String name, String description, boolean upgradable, boolean hidden,
                         TextColor color, Double defaultValue, boolean resetOnLogin, String display) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.upgradable = upgradable;
        this.hidden = hidden;
        this.description = description;
        this.color = color;
        this.defaultValue = defaultValue;
        this.resetOnLogin = resetOnLogin;
        this.display = display;
    }

    public String getDisplay() {
        return this.display.isEmpty() ? getName() + ": " + "%value%" : this.display;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeType that = (AttributeType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return id;
    }

}

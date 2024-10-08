package com.outcast.rpg.api.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class CastResult {

    private final TextComponent message;

    private CastResult(TextComponent message) {
        this.message = message;
    }

    public static CastResult empty() {
        return new CastResult(Component.text(""));
    }
    public static CastResult custom(TextComponent text) {
        return new CastResult(text);
    }
    public static CastResult success() {
        return new CastResult(Component.text(""));
    }

    public TextComponent getMessage() {
        return message;
    }

}

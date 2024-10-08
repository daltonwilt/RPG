package com.outcast.rpg.api.character.skill;

        import net.kyori.adventure.text.TextComponent;
        import org.bukkit.entity.LivingEntity;

        import java.util.function.Function;

@FunctionalInterface
public interface DescriptionArgument extends Function<LivingEntity, TextComponent> {
}

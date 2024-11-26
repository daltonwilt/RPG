package com.outcast.rpg.api.skill;

import com.outcast.rpg.util.ConversionUtil;
import com.outcast.rpg.util.exception.CastException;
import com.outcast.rpg.util.setting.PluginLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class CastErrors {

    public static CastException exceptionOf(LivingEntity user, TextComponent message) {
        return new CastException(user, message);
    }

    public static CastException failure(LivingEntity user, Castable castable) {
        return exceptionOf(user,
                Component.text("Failed to use ").color(RED)
                        .append(Component.text(castable.getName()).color(DARK_RED))
        );
    }
    public static CastException cancelled(LivingEntity user, Castable castable) {
        return exceptionOf(user,
                Component.text("Cancelled ").color(RED)
                        .append(Component.text(castable.getName()).color(DARK_RED))
        );
    }
    public static CastException onCooldown(LivingEntity user, long timestamp, Castable castable, long cooldownEnd) {
        return exceptionOf(user,
                Component.text(castable.getName()).color(DARK_RED)
                        .append(Component.text(" is on cooldown for another ").color(RED))
                        .append(Component.text(ConversionUtil.formatDuration(cooldownEnd - timestamp)).color(GRAY))
        );
    }
    public static CastException onGlobalCooldown(LivingEntity user, long timestamp, long cooldownEnd) {
        return exceptionOf(user,
                Component.text("You are on global cooldown for another ").color(RED)
                        .append(Component.text(ConversionUtil.formatDuration(cooldownEnd - timestamp)).color(GRAY))
        );
    }
    public static CastException insufficientResources(LivingEntity user, Castable castable) {
        return exceptionOf(user,
                Component.text("You do not have enough ").color(RED)
                        .append(Component.text(PluginLoader.getRPGSkillsConfig().RESOURCE_NAME).color(PluginLoader.getRPGSkillsConfig().RESOURCE_COLOR))
                        .append(Component.text(" to cast ").color(RED))
                        .append(Component.text(castable.getName()).color(DARK_RED))
        );
    }
    public static CastException blocked(LivingEntity user, Castable castable) {
        return exceptionOf(user,
                Component.text(castable.getName()).color(DARK_RED)
                        .append(Component.text(" has been blocked").color(RED))
        );
    }
    public static CastException noPermission(LivingEntity user, Castable castable) {
        return exceptionOf(user,
                Component.text("You don't have permission to use the skill ").color(RED)
                        .append(Component.text(castable.getName()).color(DARK_RED))
        );
    }

    public static CastException noTarget(LivingEntity user) {
        return exceptionOf(user, Component.text("No target could be found").color(RED));
    }
    public static CastException invalidTarget(LivingEntity user) {
        return exceptionOf(user, Component.text("Target is not valid").color(RED));
    }
    public static CastException obscuredTarget(LivingEntity user) {
        return exceptionOf(user, Component.text("Target is obscured").color(RED));
    }
    public static CastException notImplemented(LivingEntity user) {
        return exceptionOf(user, Component.text("This skill is not implemented yet").color(RED));
    }
    public static CastException noSuchSkill(LivingEntity user) {
        return exceptionOf(user, Component.text("No such skill exists").color(RED));
    }
    public static CastException internalError(LivingEntity user) {
        return exceptionOf(user, Component.text("An internal error occurred while casting this skill. Please report to staff").color(RED));
    }
    public static CastException invalidArgs(LivingEntity user) {
        return exceptionOf(user, Component.text("Invalid arguments were supplied to the skill").color(RED));
    }

}

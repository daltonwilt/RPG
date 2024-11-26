package com.outcast.rpg.api.character.skill;

import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.util.ConversionUtil;
import com.outcast.rpg.util.setting.PluginLoader;
import com.udojava.evalex.Expression;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;
import org.javatuples.Pair;

import java.util.Map;
import java.util.Objects;

public abstract class Skill implements Castable {

    private final String id;
    private final String name;
    private final String permission;
    private final TextComponent description;
    private Pair<String, ?>[] descriptionArgs;
    @Setter
    private String cooldownExpression;
    @Setter
    private String resourceCostExpression;
    @Setter
    private Map<String, String> properties;

    public Skill(SkillSpec skillSpec) {
        this.id = skillSpec.getId();
        this.name = skillSpec.getName();
        this.permission = skillSpec.getPermission();
        this.description = skillSpec.getDescription();
        this.descriptionArgs = skillSpec.getDescriptionArgs();
        this.cooldownExpression = skillSpec.getCooldownExpression();
        this.resourceCostExpression = skillSpec.getResourceExpression();
        this.properties = skillSpec.getProperties();
    }

//    // I may end up changing the way to evaluate expressions
    protected static Expression asExpression(String expression) {
        return PluginLoader.getExpressionService().getExpression(expression);
    }

    protected static double asDouble(LivingEntity source, String exp) {
        return PluginLoader.getExpressionService().evalExpression(source, exp).doubleValue();
    }

    protected static double asDouble(LivingEntity source, LivingEntity target, String exp) {
        return PluginLoader.getExpressionService().evalExpression(source, target, exp).doubleValue();
    }

    protected static int asInt(LivingEntity source, String exp) {
        return PluginLoader.getExpressionService().evalExpression(source, exp).intValue();
    }

    protected static int asInt(LivingEntity source, LivingEntity target, String exp) {
        return PluginLoader.getExpressionService().evalExpression(source, target, exp).intValue();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TextComponent getDescription(LivingEntity living) {
        return PluginLoader.getSkillGraphFacade().renderSkillDescription(living, description, descriptionArgs);
    }

    @Override
    public long getCooldown(LivingEntity living) {
        return PluginLoader.getSkillGraphFacade().getSkillCooldown(living, cooldownExpression);
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public double getResource(LivingEntity living) {
        return PluginLoader.getSkillGraphFacade().getSkillResourceCost(living, resourceCostExpression);
    }

    @SafeVarargs
    protected final void setDescriptionArgs(Pair<String, String>... descriptionArgs) {
        this.descriptionArgs = descriptionArgs;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String propertyKey, Class<T> asClass, T defaultValue) {
        if (String.class.equals(asClass)) {
            return (T) properties.getOrDefault(propertyKey, (String) defaultValue);
        }

        if (Integer.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Integer) defaultValue);
        }

        if (Double.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Double) defaultValue);
        }

        if (Float.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Float) defaultValue);
        }

        if (Long.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Long) defaultValue);
        }

        if (Byte.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Byte) defaultValue);
        }

        if (Short.class.equals(asClass)) {
            return (T) ConversionUtil.valueOf(properties.get(propertyKey), (Short) defaultValue);
        }

        return defaultValue;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Castable skill)) return false;
        return id.equals(skill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

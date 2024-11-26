package com.outcast.rpg.character;

import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.character.attribute.AttributeType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NPCharacter<T extends LivingEntity> implements RPGCharacter<T> {

    private final UUID id;
    @Setter
    private T entity;
    @Getter
    private String name;

    @Setter
    private Map<AttributeType, Double> characterAttributes;
    private final Map<AttributeType, Double> buffAttributes;

    public NPCharacter(T entity, Map<AttributeType, Double> characterAttributes) {
        this.id = entity.getUniqueId();
        this.entity = entity;
        this.characterAttributes = characterAttributes;
        this.buffAttributes = new HashMap<>();
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Optional<T> getEntity() {
        return Optional.ofNullable(entity);
    }

    @Override
    public Map<AttributeType, Double> getCharacterAttributes() {
        return characterAttributes;
    }
    @Override
    public void setCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.put(type, value);
    }
    @Override
    public void addCharacterAttribute(AttributeType type, Double amount) {
        characterAttributes.merge(type, amount, Double::sum);
    }

    @Override
    public Map<AttributeType, Double> getBuffAttributes() {
        return buffAttributes;
    }
    @Override
    public void mergeBuffAttributes(Map<AttributeType, Double> additional) {
        additional.forEach((type, value) -> buffAttributes.merge(type, value, Double::sum));
    }

}

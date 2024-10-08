package com.outcast.rpg.api.character;

import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.db.SpigotIdentifiable;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Optional;

public interface RPGCharacter<T extends LivingEntity> extends SpigotIdentifiable {

    Optional<T> getEntity();

    void setEntity(T entity);

    /**
     * Gets the current Character Attributes
     * @return Map of Character Attributes
     */
    Map<AttributeType, Double> getCharacterAttributes();

    /**
     * Set a Character attribute to a specific value
     * @param type Attribute to set
     * @param value Value to set to
     */
    void setCharacterAttribute(AttributeType type, Double value);

    /**
     * Add a value to an existing character attribute
     * @param type Attribute to add
     * @param amount Amount to add
     */
    void addCharacterAttribute(AttributeType type, Double amount);

    /**
     * Gets the current Buffed Attributes
     * @return Map of Attributes provided by buffs
     */
    Map<AttributeType, Double> getBuffAttributes();

    /**
     * Merge the values of the two attribute type maps by adding them together
     * @param additional Attributes to add
     */
    void mergeBuffAttributes(Map<AttributeType, Double> additional);

}

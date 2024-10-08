package com.outcast.rpg.service.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.attribute.AttributeTypeRegistry;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.attribute.AttributesConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class AttributeService {

    @Inject
    private RPGConfig config;
    @Inject
    private AttributesConfig attributesConfig;
    @Inject
    private AttributeTypeRegistry attributeTypeRegistry;

    @Inject
    private CharacterService characterService;

    private Map<AttributeType, Double> defaultAttributes;

    public AttributeService() {}

    /**
     * Returns a map containing the default values configured for each attribute
     * @return Map
     */
    public Map<AttributeType, Double> getDefaultAttributes() {
        if(defaultAttributes == null) {
            defaultAttributes = new HashMap<>();
            for(AttributeType type : attributeTypeRegistry.getAll()) {
                defaultAttributes.put(type, Math.max(0, type.getDefaultValue()));
            }
        }
        return defaultAttributes;
    }

    /**
     * Set any missing attributes to 0
     * @param attributes Attribute hashmap to modify
     * @return modified attributes with missing attributes set to 0
     */
    public Map<AttributeType, Double> fillAttributes(Map<AttributeType, Double> attributes) {
        for (AttributeType type : attributeTypeRegistry.getAll()) {
            attributes.putIfAbsent(type, 0.0);
        }
        return attributes;
    }

    /**
     * Merge the values of the two attribute type maps.<br>
     * WARNING: This will ALTER the source map
     *
     * @param source     The map to be altered
     * @param additional The additional attributes to be added
     */
    private void mergeAttributes(Map<AttributeType, Double> source, Map<AttributeType, Double> additional) {
        additional.forEach((type, value) -> source.merge(type, value, Double::sum));
    }

    public Map<AttributeType, Double> getBuffAttributes(RPGCharacter<?> character) {
        return new HashMap<>(character.getBuffAttributes());
    }

    public Map<AttributeType, Double> getAllAttributes(Entity entity) {
        RPGCharacter<?> character = characterService.getOrCreateCharacter(entity);

        Map<AttributeType, Double> results = new HashMap<>();

        if (entity instanceof Player) {
            mergeAttributes(results, getDefaultAttributes());
        }

        mergeAttributes(results, character.getCharacterAttributes());
//        mergeAttributes(results, getEquipmentAttributes(entity));
        mergeAttributes(results, character.getBuffAttributes());

        results.replaceAll((key, value) -> Math.max(0.0d, value));
        return results;
    }

    // Add in equipment attributes later

}

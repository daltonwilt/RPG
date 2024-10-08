package com.outcast.rpg.character.attribute;

import com.outcast.rpg.config.attribute.AttributesConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class AttributeTypeRegistry {

    private static final Map<String, AttributeType> attributeTypeMap = new LinkedHashMap<>();

    public AttributeTypeRegistry() {
        try {
            AttributesConfig config = new AttributesConfig();
            config.init();

            config.ATTRIBUTE_TYPES.stream().map(conf -> new AttributeType(
                    conf.getId(),
                    conf.getShortName(),
                    conf.getName(),
                    conf.getDescription(),
                    conf.isUpgradable(),
                    conf.isHidden(),
                    conf.getColor(),
                    conf.getDefaultValue(),
                    conf.isResetOnLogin(),
                    conf.getDisplay()
            )).forEach(type -> attributeTypeMap.put(type.getId(), type));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerAttribute(AttributeType type) {
        attributeTypeMap.put(type.getId(), type);
    }

    public static Optional<AttributeType> getById(String id) {
        return Optional.ofNullable(attributeTypeMap.get(id));
    }

    public Collection<AttributeType> getAll() {
        return attributeTypeMap.values();
    }

}

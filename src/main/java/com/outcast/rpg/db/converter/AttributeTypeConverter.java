package com.outcast.rpg.db.converter;

import com.outcast.rpg.character.attribute.AttributeType;
import jakarta.persistence.AttributeConverter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;

public class AttributeTypeConverter implements AttributeConverter<AttributeType, String> {

    @Override
    public String convertToDatabaseColumn(AttributeType attribute) {
        return attribute.getId();
    }

    @Override
    public AttributeType convertToEntityAttribute(String dbData) {
//        parse dbData string to create an AttributeType
        return new AttributeType(
                "",
                "",
                "",
                "",
                false,
                false,
                NamedTextColor.WHITE,
                0.0,
                false,
                ""
                );
    }

}

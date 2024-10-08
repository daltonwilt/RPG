package com.outcast.rpg.util.serialize;

import com.google.common.reflect.TypeToken;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class TextColorTypeSerializer implements TypeSerializer<TextColor> {

    @Override
    public TextColor deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String val = value.getString();

        if (StringUtils.isEmpty(val)) {
            throw new ObjectMappingException("Cannot parse ChatColor: Is either null or empty string.");
        }

        return TextColor.fromHexString(val);
    }

    @Override
    public void serialize(TypeToken<?> type, TextColor obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(Objects.requireNonNull(obj).asHexString());
    }

}

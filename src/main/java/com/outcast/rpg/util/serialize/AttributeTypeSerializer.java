package com.outcast.rpg.util.serialize;

import com.google.common.reflect.TypeToken;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.attribute.AttributeTypeRegistry;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class AttributeTypeSerializer implements TypeSerializer<AttributeType> {

    @Override
    public @Nullable AttributeType deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        String val = value.getString();

        if (StringUtils.isEmpty(val)) {
            throw new ObjectMappingException("Cannot parse ChatColor: Is either null or empty string.");
        }

        return AttributeTypeRegistry.getById(val).get();
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable AttributeType obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
        value.setValue(Objects.requireNonNull(obj).getId());
    }

}

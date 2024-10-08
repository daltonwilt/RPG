package com.outcast.rpg.db.converter;

import com.outcast.rpg.character.role.Role;
import com.outcast.rpg.util.setting.PluginLoader;
import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getName();
    }

    @Override
    public Role convertToEntityAttribute(String roleId) {
        return PluginLoader.getCharacterService().getRole(roleId).orElse(Role.knave);
    }

}

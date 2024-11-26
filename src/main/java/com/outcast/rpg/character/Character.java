package com.outcast.rpg.character;

import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.role.Role;
import com.outcast.rpg.db.converter.AttributeTypeConverter;
import com.outcast.rpg.db.converter.RoleConverter;
import com.outcast.rpg.db.converter.SkillConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(schema = "rpg", name = "Character")
public class Character implements RPGCharacter<Player> {

    @Id
    private UUID id;

    @Transient
    private Player entity;

    @Setter
    @Transient
    private boolean hasJoined;

    /**
     * Attributes the player has leveled up, not including the default attribute amount
     * from the configuration
     * Only Upgradable Attributes should be stored within this map
     */
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "attribute_type")
    @Column(name = "value")
    @CollectionTable(schema = "rpg", name = "Character_attributes")
    @Convert(attributeName = "key.", converter = AttributeTypeConverter.class)
    private Map<AttributeType, Double> characterAttributes = new ConcurrentHashMap<>();

    /**
     * Attributes that come from temporary sources
     */
    @Transient
    private final Map<AttributeType, Double> buffAttributes = new HashMap<>();

    @Setter
    @Getter
    private double experience;
    @Setter
    @Getter
    private Integer level;

    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<Material, String> binds =  new ConcurrentHashMap<>();

    @Getter
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "rpg", name = "Character_skills")
    @Convert(converter = SkillConverter.class)
    private List<Skill> skills = new ArrayList<>();

    @Transient
    private List<Skill> roleSkills;

    @Convert(converter = RoleConverter.class)
    private Role character = Role.knave;

    public Character() {}
    public Character(UUID id) {
        this.id = id;
    }

    @NotNull
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Optional<Player> getEntity() {
        return Optional.ofNullable(entity);
    }
    @Override
    public void setEntity(Player entity) {
        this.entity = entity;
    }

    public boolean hasJoined() {
        return hasJoined;
    }

    @Override
    public Map<AttributeType, Double> getCharacterAttributes() {
        return Collections.unmodifiableMap(characterAttributes);
    }
    @Override
    public void setCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.put(type, value);
    }

    @Override
    public void addCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.merge(type, value, Double::sum);
    }

    @Override
    public Map<AttributeType, Double> getBuffAttributes() {
        return Collections.unmodifiableMap(buffAttributes);
    }
    @Override
    public void mergeBuffAttributes(Map<AttributeType, Double> additional) {
        additional.forEach((type, value) -> buffAttributes.merge(type, value, Double::sum));
    }

    public Map<String, Long> getCooldowns() {
        return Collections.unmodifiableMap(cooldowns);
    }
    public void setCooldowns(String role, Long value) {
        cooldowns.put(role, value);
    }

    public Map<Material, String> getBinds() {
        return Collections.unmodifiableMap(binds);
    }
    public void setBinds(Material material, String value) {
        binds.put(material, value);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    public void removeSkill(String skill) {
        skills.remove(skill);
    }

    public Role getRole() {
        return character;
    }
    public void setRole(Role role) {
        this.character = role;
    }

}

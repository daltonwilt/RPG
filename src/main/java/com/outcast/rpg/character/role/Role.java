package com.outcast.rpg.character.role;

import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.util.Enum;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;

public class Role implements Comparable<Role> {

    public static final Role knave = new Role("Knave", new HashSet<>(), "Test Description");

    @Getter
    private String name;
    @Getter
    private final Set<Skill> skills;
    @Getter
    private String description;

    @Getter
    private int maxLevel;
    @Getter
    private double cost;
    @Getter
    private double expModifier;

    private final Map<AttributeType, Double> baseAttributes = new HashMap<>();

    private final Map<Material, Double> itemDamage = new EnumMap<>(Material.class);
    private final Map<EntityType, Double> projectileDamage = new EnumMap<>(EntityType.class);

    private final Set<Material> armor = new HashSet<>();
    private final Set<Material> mainHand = new HashSet<>();
    private final Set<Material> offHand = new HashSet<>();

    private Set<Enum.ExperienceType> experienceTypes = EnumSet.noneOf(Enum.ExperienceType.class);

    public Role(String name, Set<Skill> skills, String description) {
        this.name = name;
        this.skills = skills;
        this.description = description;
        this.maxLevel = 1;
        this.cost = 0.0;
        this.expModifier = 1.0;
    }

//======================================================================================================================
//     Getters / Setters
//======================================================================================================================

    protected void setName(String n) {
        this.name = n;
    }

    public Skill getSkillById(String id) {
        Skill s = null;
        for(Iterator<Skill> it = skills.iterator(); it.hasNext();) {
            if(it.next().getId().equals(id)) {
                s = it.next();
            }
        }
        return s;
    }
    public Skill getSkill(Skill skill) {
        if(skill == null)
            return null;
        return getSkillById(skill.getId());
    }

    protected void setDescription(String d) {
        this.description = d;
    }

    protected void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    protected void setCost(double cost) {
        this.cost = cost;
    }

    protected void setExpModifier(double modifier) {
        this.expModifier = modifier;
    }


    public Double getBaseAttributes(AttributeType a) {
        return this.baseAttributes.get(a);
    }
    public void setBaseAttributes(AttributeType a, Double amount) {
        this.baseAttributes.put(a, amount);
    }

    @Nullable
    public Double getItemDamage(Material m) {
        return this.itemDamage.get(m);
    }
    protected void setItemDamage(Material m, Double damage) {
        this.itemDamage.put(m, damage);
    }

    @Nullable
    public Double getProjectileDamage(EntityType p) {
        return this.projectileDamage.get(p);
    }
    protected void setProjectileDamage(EntityType p, Double damage) {
        this.projectileDamage.put(p, damage);
    }

    public Set<Material> getArmor() {
        return Collections.unmodifiableSet(this.armor);
    }
    protected void addArmor(Material armor) {
        this.armor.add(armor);
    }

    public Set<Material> getMainHand() {
        return Collections.unmodifiableSet(this.mainHand);
    }
    protected void addMainHand(Material weapon) {
        this.mainHand.add(weapon);
    }

    public Set<Material> getOffhand() {
        return Collections.unmodifiableSet(this.offHand);
    }
    protected void addOffhand(Material weapon) {
        this.offHand.add(weapon);
    }

    public Set<Material> getAllowedArmor() {
        return Collections.unmodifiableSet(this.armor);
    }
    public boolean isAllowedArmor(Material m) {
        return this.armor.contains(m);
    }

    public Set<Material> getAllowedMainHand() {
        return Collections.unmodifiableSet(this.mainHand);
    }
    public boolean isAllowedMainHand(Material m) {
        return this.mainHand.contains(m);
    }

    public Set<Material> getAllowedOffHand() {
        return Collections.unmodifiableSet(this.offHand);
    }
    public boolean isAllowedOffHand(Material m) {
        return this.offHand.contains(m);
    }

    public boolean hasExperienceType(Enum.ExperienceType type) {
        return this.experienceTypes.contains(type);
    }
    public Set<Enum.ExperienceType> getExperienceType() {
        return Collections.unmodifiableSet(this.experienceTypes);
    }
    protected void setExperienceType(Set<Enum.ExperienceType> experienceTypes) {
        this.experienceTypes = experienceTypes;
    }

//======================================================================================================================
//    Utility Methods
//======================================================================================================================

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return (this.name == null) ? 0 : this.name.hashCode();
    }

    public int compareTo(Role character) {
        return this.name.compareTo(character.name);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Role role))
            return false;
        return this.name.equalsIgnoreCase(role.name);
    }

}

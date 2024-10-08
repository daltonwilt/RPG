package com.outcast.rpg.service.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.api.character.ResourceUser;
import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.character.Character;
import com.outcast.rpg.character.NPCharacter;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.role.Role;
import com.outcast.rpg.skills.BlankSkill;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.archetype.ArchetypesConfig;
import com.outcast.rpg.config.archetype.RoleConfig;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.skill.SkillGraphService;
import com.outcast.rpg.util.setting.PluginLoader;
import com.udojava.evalex.Expression;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CharacterService {

    @Inject private RPGConfig rpgConfig;
    @Inject private ArchetypesConfig archetypesConfig;

//    @Inject private CharacterRepository repository;

    @Inject private AttributeService attributeService;
    @Inject private ExpressionService expressionService;
    @Inject private SkillGraphService skillGraphService;

    private final Map<UUID, RPGCharacter<? extends LivingEntity>> NPCs = new HashMap<>();
    private final Map<String, Role> roles = new HashMap<>();

    // Temporarily store
    Map<UUID, Character> characters = new HashMap<>();

    public void init() {
        for(RoleConfig roleConfig : archetypesConfig.ROLE_CONFIGS) {
            Role role = new Role(
                    roleConfig.NAME,
                    roleConfig.SKILLS.stream()
                            .map(skillId -> (Skill) PluginLoader.getSkillService().getSkillById(skillId).get())
                            .collect(Collectors.toSet()),
                    roleConfig.DESCRIPTION
            );
            roles.put(roleConfig.NAME, role);
        }
    }

    private Optional<Player> getPlayer(Character c) {
        return c.getEntity();
    }

    public Character getOrCreateCharacter(Player player) {
//        return repository.findById(player.getUniqueId()).orElseGet(() -> {
        Character c;
        if(characters.get(player.getUniqueId()) != null) {
            c = characters.get(player.getUniqueId());
        } else {
            c = new Character(player.getUniqueId());
            c.setEntity(player);
            c.addSkill(BlankSkill.blank);
            c.setExperience(0);
//            repository.saveOne(c);
            characters.put(player.getUniqueId(), c);
        }
            return c;
//        });
    }
    public RPGCharacter<? extends LivingEntity> getOrCreateCharacter(LivingEntity living, Map<AttributeType, Double> attributes) {
        if (living instanceof Player) {
            return getOrCreateCharacter((Player) living);
        }

        RPGCharacter<? extends LivingEntity> npc = NPCs.get(living.getUniqueId());

        if (npc == null) {
            npc = new NPCharacter<>(living, attributes);
            NPCs.put(living.getUniqueId(), npc);
        }

        return npc;
    }
    public<T extends Entity> RPGCharacter<?> getOrCreateCharacter(T entity) {
        if(entity instanceof LivingEntity living) {
            return getOrCreateCharacter((LivingEntity) entity, attributeService.getDefaultAttributes());
        } else {
            throw new IllegalArgumentException("Entity must be some sort of LivingEntity.");
        }
    }

    public void setSkills(Character c, List<Skill> skills) {
        setSkillPermissions(c, c.getSkills(), false);
        c.getSkills().clear();
        c.addSkill(skillGraphService.getSkillGraphRoot());
        setSkillPermission(c, skillGraphService.getSkillGraphRoot(), true);
        c.getSkills().addAll(skills);
        setSkillPermissions(c, skills, true);
//        repository.saveOne(c);
    }
    public void addSkill(Character c, Skill skill) {
        c.addSkill(skill);
        setSkillPermission(c, skill, true);
//        repository.saveOne(c);
    }
    public void removeSkill(Character c, Skill skill) {
        c.removeSkill(skill.getId());
        setSkillPermission(c, skill, false);
    }

    private void setSkillPermission(Character c, Castable skill, boolean value) {
        getPlayer(c).ifPresent(player -> {
            player.addAttachment(RPG.getInstance(), skill.getPermission(), value);
        });
    }
    private void setSkillPermissions(Character c, Collection<? extends Castable> skills, boolean value) {
        getPlayer(c).ifPresent(player -> {
            skills.forEach(skill -> {
                player.addAttachment(RPG.getInstance(), skill.getPermission(), value);
            });
        });
    }

    public void addExperience(Character c, double amount) {
        c.setExperience(c.getExperience() + amount);
//        repository.saveOne(c);
    }
    public void removeExperience(Character c, double amount) {
        c.setExperience(c.getExperience() - amount);
//        repository.saveOne(c);
    }

    public void addAttribute(Character c, AttributeType attributeType, double amount) {
        c.addCharacterAttribute(attributeType, amount);
//        repository.saveOne(c);
//        Sponge.getEventManager().post(new ChangeAttributeEvent(c));
    }
    public void setAttribute(Character c, AttributeType attributeType, double amount) {
        c.setCharacterAttribute(attributeType, amount);
//        repository.saveOne(c);
//        Sponge.getEventManager().post(new ChangeAttributeEvent(c));
    }
    public void removeAttribute(Character c, AttributeType attributeType, double amount) {
        Double current = c.getCharacterAttributes().getOrDefault(attributeType, 0.0d);
        c.setCharacterAttribute(attributeType, Math.max(0.0d, current - amount));
//        repository.saveOne(c);
//        Sponge.getEventManager().post(new ChangeAttributeEvent(c));
    }
    public void mergeBuffAttributes(RPGCharacter<?> c, Map<AttributeType, Double> attributes) {
        c.mergeBuffAttributes(attributes);
    }

    public double calcResourceRegen(Map<AttributeType, Double> attributes) {
        Expression expression = expressionService.getExpression(rpgConfig.RESOURCE_REGEN_CALCULATION);
        expressionService.populateSourceAttributes(expression, attributes);
        return expression.eval().doubleValue();
    }

    public void resetCharacterSkills(Character c) {
        setSkills(c, new ArrayList<>(c.getRole().getSkills()));
//        repository.saveOne(pc);
    }
    public void resetCharacterAttributes(Character c) {
        c.setCharacterAttributes(new HashMap<>());
//        repository.saveOne(pc);
//        Sponge.getEventManager().post(new ChangeAttributeEvent(pc));
    }
    public void resetCharacter(Character c) {
        resetCharacterSkills(c);
        resetCharacterAttributes(c);
    }

    public void setRole(Character c, Role role) {
        c.setRole(role);
        resetCharacterSkills(c);
    }
    public Optional<Role> getRole(String roleId) {
        return Optional.ofNullable(roles.get(roleId));
    }
    public Collection<Role> getAllRoles() {
        return roles.values();
    }

    public void assignEntityResourceLimit(LivingEntity player, boolean fill) {
        double max = expressionService.evalExpression(player, rpgConfig.RESOURCE_LIMIT_CALCULATION).doubleValue();
        ResourceUser user = PluginLoader.getResourceService().getOrCreateUser(player);

        user.setMax(max);
        if (fill) {
            user.fill();
        } else if (user.getCurrent() > user.getMax()) {
            user.fill();
        }
    }

    public void assignEntityHealthLimit(LivingEntity living, boolean fill) {
        double maxHP = expressionService.evalExpression(living, rpgConfig.HEALTH_LIMIT_CALCULATION).doubleValue();

//        DataTransactionResult maxHPResult = living.offer(Keys.MAX_HEALTH, maxHP);
//        if (fill) {
//            living.offer(Keys.HEALTH, maxHP);
//        }
//
//        if (!maxHPResult.isSuccessful()) {
//            RPG.warn("Failed to set max health for entity %s, Max HP Result: %s", living, maxHPResult);
//        }
//
//        if (living.supports(Keys.HEALTH_SCALE)) {
//            living.offer(Keys.HEALTH_SCALE, config.HEALTH_SCALING);
//        }
    }

    public void assignEntityMovementSpeed(LivingEntity living) {
        if (living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) return;

        double newMovementSpeed = expressionService.evalExpression(living, rpgConfig.MOVEMENT_SPEED_CALCULATION).doubleValue();
        double oldMovementSpeed = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();

        if (Math.abs(newMovementSpeed - oldMovementSpeed) >= 0.0001) {
            living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(newMovementSpeed);
        }
    }

}

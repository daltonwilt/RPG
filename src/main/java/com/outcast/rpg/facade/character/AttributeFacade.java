package com.outcast.rpg.facade.character;

import com.google.inject.Inject;
import com.outcast.rpg.api.character.RPGCharacter;
import com.outcast.rpg.character.Character;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.attribute.AttributesConfig;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.service.character.CharacterService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class AttributeFacade {

    private Map<AttributeType, Double> defaultAttributes;

    @Inject
    private RPGConfig rpgConfig;
    @Inject
    private AttributesConfig attributesConfig;

    @Inject
    private AttributeService attributeService;
    @Inject
    private CharacterService characterService;
    @Inject
    private ExpressionService expressionService;

    public void init() {}

    /**
     * Adds a CharacterAttribute to a players character
     * @param player The Player to add the attribute for
     * @param attributeType The attribute to add
     * @param amount The amount to add
     * @throws CommandException
     */
    public void addPlayerAttribute(Player player, AttributeType attributeType, double amount) throws CommandException {
        Character c = characterService.getOrCreateCharacter(player);

        if (validateCharacterAttribute(attributeType, c.getCharacterAttributes().getOrDefault(attributeType, rpgConfig.ATTRIBUTE_MIN) + amount)) {
            characterService.addAttribute(c, attributeType, amount);
        }
    }

    /**
     * Removes a CharacterAttribute from a players character
     * @param player The player to remove the attribute for
     * @param attributeType The Attribute to remove
     * @param amount The amount to remove
     * @throws CommandException
     */
    public void removePlayerAttribute(Player player, AttributeType attributeType, double amount) throws CommandException {
        Character c = characterService.getOrCreateCharacter(player);

        if (validateCharacterAttribute(attributeType, c.getCharacterAttributes().getOrDefault(attributeType, rpgConfig.ATTRIBUTE_MIN) - amount)) {
            characterService.removeAttribute(c, attributeType, amount);
        }
    }

    /**
     * Validate setting the value of an Attribute against a Character
     * @param type The Attribute to be set
     * @param amount The amount to the attribute will be set to
     * @return true if this is a valid action
     * @throws CommandException When the Attribute is invalid
     */
    private boolean validateCharacterAttribute(AttributeType type, Double amount) throws CommandException {
        if (!type.isUpgradable()) {
            throw new CommandException("Cannot set a Non-Upgradable attribute against a Character");
        }
        if (amount < rpgConfig.ATTRIBUTE_MIN) {
            throw new CommandException("A player cannot have attributes less than " + rpgConfig.ATTRIBUTE_MIN);
        }
        if (amount > rpgConfig.ATTRIBUTE_MAX) {
            throw new CommandException("A player cannot have attributes bigger than " + rpgConfig.ATTRIBUTE_MAX);
        }
        return true;
    }

    public void mergeBuffAttributes(RPGCharacter<?> c, Map<AttributeType, Double> attributes) {
        characterService.mergeBuffAttributes(c, attributes);
    }

    public void resetPlayerAttributes(Player player) {
        characterService.resetCharacterAttributes(characterService.getOrCreateCharacter(player));
        player.sendMessage(GREEN + "Your attributes have been reset.");
    }

    public Map<AttributeType, Double> getAllAttributes(Entity entity) {
        return attributeService.getAllAttributes(entity);
    }

}

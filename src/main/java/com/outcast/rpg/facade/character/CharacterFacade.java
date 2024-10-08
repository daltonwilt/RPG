package com.outcast.rpg.facade.character;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.event.ResourceEvent;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.api.skill.Castable;
import com.outcast.rpg.character.Character;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.role.Role;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.archetype.ArchetypeConfig;
import com.outcast.rpg.config.archetype.ArchetypesConfig;
import com.outcast.rpg.facade.skill.SkillGraphFacade;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.CharacterService;
import com.outcast.rpg.util.setting.PluginLoader;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.*;

@Singleton
public class CharacterFacade {

    private final TextComponent NEW_LINE = Component.newline();

    @Inject private RPGConfig rpgConfig;
    @Inject private ArchetypesConfig archetypesConfig;

    @Inject private CharacterService characterService;
    @Inject private ExpressionService expressionService;
//    @Inject private DamageService damageService;
//    @Inject private HealingService healingService;

    @Inject private AttributeFacade attributeFacade;
    @Inject private SkillGraphFacade skillGraphFacade;

    private static final Sound roleSound = Sound.sound(Key.key("cave"), Sound.Source.AMBIENT, 1f, 1f);

    public void addPlayerExperience(Player player, double amount) {
        Character c = characterService.getOrCreateCharacter(player);
        characterService.addExperience(c, amount);
        player.sendMessage(ChatColor.GREEN + "You have gained " + ChatColor.GOLD + amount + ChatColor.GREEN + " experience.");
    }
    public void removePlayerExperience(Player player, double amount) {
        Character c = characterService.getOrCreateCharacter(player);
        characterService.removeExperience(c, amount);
    }

    public void displayAllSkills(Player player) throws CommandException {
        Character c = characterService.getOrCreateCharacter(player);
        checkRole(c);

        List<Skill> ownedSkills =  rpgConfig.DISPLAY_ROOT_SKILL ? c.getSkills() : c.getSkills().subList(1, c.getSkills().size());

        List<TextComponent> messages = new ArrayList<>();
        messages.add(Component.text()
                .content(getArchetype(ownedSkills))
                .color(AQUA)
                .decorate(TextDecoration.BOLD)
                .build());

        messages.add(Component.text()
                .append(Component.text().content("[]============[ ").color(DARK_GRAY))
                .append(Component.text().content("Skills").color(DARK_AQUA))
                .append(Component.text().content(" ]============[]").color(DARK_GRAY))
                .build());

        for(Skill skill : ownedSkills) {
            TextComponent description = Component.text("  ")
                    .append(skillGraphFacade.renderAvailableSkill(skill, player, true).color(DARK_GRAY));
            messages.add(description);
        }

        messages.add(Component.text().content("").build());
        messages.forEach(player::sendMessage);
    }

    private void checkRole(Character c) throws CommandException {
        if(archetypesConfig.ROLE_CONFIGS.size() > 0 && c.getRole() == Role.knave) {
            throw new CommandException("You must choose a class before unlocking skills!");
        }
    }
    public void displayRoles(Player player) throws CommandException {
        Character c = characterService.getOrCreateCharacter(player);
        List<TextComponent> messages = new ArrayList<>();

        messages.add(Component.text()
                .append(Component.text().content("[]============[ ").color(DARK_GRAY))
                .append(Component.text().content("Roles").color(DARK_AQUA))
                .append(Component.text().content(" ]============[]").color(DARK_GRAY))
                .build());

        for(Role role : characterService.getAllRoles()) {
            TextComponent name = Component.text()
                    .append(Component.text().content("  " + role.getName()).color(DARK_GRAY))
                    .build();
            messages.add(name);
        }

        messages.add(Component.text().content("").build());
        messages.forEach(player::sendMessage);
    }
    public void setRole(CommandSender sender, Player player, String id) throws CommandException {
        Role role = characterService.getRole(id).orElseThrow(() -> new CommandException("No class with that name exists."));
        characterService.setRole(characterService.getOrCreateCharacter(player), role);
        sender.sendMessage(ChatColor.GREEN + player.getName() + "'s class changed to " + ChatColor.GOLD + role.getName() + ChatColor.GREEN + ".");
    }

    private String getArchetype(List<Skill> skills) {
        int skillCount = 0;
        String finalArchetype = archetypesConfig.DEFAULT;

        for (ArchetypeConfig archetype : archetypesConfig.ARCHETYPES) {
            Set<String> archetypeSkills = new HashSet<>(archetype.SKILLS);
            archetypeSkills.retainAll(skills.stream().map(Skill::getId).collect(Collectors.toList()));

            if (archetypeSkills.size() > skillCount) {
                skillCount = archetypeSkills.size();
                finalArchetype = archetype.NAME;
            }
        }

        return finalArchetype;
    }

    public void onPlayerJoin(Player player) {
        Character c = characterService.getOrCreateCharacter(player);
        boolean fill = false;

        if(!c.hasJoined()) {
            c.setHasJoined(true);
            fill = true;
        }

        updateCharacter(player, fill);
    }
    public void onPlayerRespawn(Player player) {
        updateCharacter(player, true);
    }

    public void resetPlayerCharacter(Player player) {
        characterService.resetCharacter(characterService.getOrCreateCharacter(player));
    }
    public void displayPlayerCharacter(Player player) {
        Character c = characterService.getOrCreateCharacter(player);

        List<TextComponent> messages = new ArrayList<>();

        messages.add(Component.text()
                .append(Component.text().content("[]============[ ").color(DARK_GRAY))
                .append(Component.text().content("Character").color(DARK_AQUA))
                .append(Component.text().content(" ]============[]").color(DARK_GRAY))
                .build());

        messages.add(Component.text()
                .append(Component.text().content("  Role: " + c.getRole().getName()).color(DARK_GRAY))
                .build());

        messages.add(Component.text()
                .append(Component.text().content("  Attributes: ").color(DARK_GRAY))
                .build());
        for(AttributeType type : c.getCharacterAttributes().keySet()) {
            TextComponent name = Component.text()
                    .append(Component.text().content("  "+ type.getName() +": "+ c.getCharacterAttributes().get(type)).color(DARK_GRAY))
                    .build();
            messages.add(name);
        }

        messages.add(Component.text()
                .append(Component.text().content("  Skills: ").color(DARK_GRAY))
                .build());
        for(Skill skill :  rpgConfig.DISPLAY_ROOT_SKILL ? c.getSkills() : c.getSkills().subList(1, c.getSkills().size())) {
            TextComponent name = Component.text()
                    .append(Component.text().content("    "+ skill.getName()).color(DARK_GRAY))
                    .build();
            messages.add(name);
        }

        messages.add(Component.text()
                .append(Component.text().content("  Experience: "+ c.getExperience()).color(DARK_GRAY))
                .build());

        messages.add(Component.text()
                .append(Component.text().content("  Level: "+ c.getLevel()).color(DARK_GRAY))
                .build());

        messages.add(Component.text().content("").build());
        messages.forEach(player::sendMessage);

        Collection<Component> bookPages = new ArrayList<>();
        TextComponent firstPage = Component.text()
                .append(Component.text("> Character")
                        .hoverEvent(HoverEvent.showText(Component.text("Display Character")))
                ).append(NEW_LINE)
                .append(Component.text("> Roles")
                        .hoverEvent(HoverEvent.showText(Component.text("Display Character")))
                ).append(NEW_LINE)
                .append(Component.text("> Skills")
                        .hoverEvent(HoverEvent.showText(Component.text("Display Character")))
                ).append(NEW_LINE)
                .append(Component.text("> Commands")
                        .hoverEvent(HoverEvent.showText(Component.text("Display Commands")))
                ).append(NEW_LINE)
                .append(Component.text("> Settings")
                        .hoverEvent(HoverEvent.showText(Component.text("Display Settings")))
                ).append(NEW_LINE)
                .build();

        bookPages.add(firstPage);

        Book book = Book.book(Component.text("Character"), Component.text("Server"), bookPages);
        player.openBook(book);
    }
    public void updateCharacter(LivingEntity living, boolean fill) {
//        characterService.assignEntityHealthLimit(living, fill);
//        characterService.assignEntityResourceLimit(living, fill);
        if(living instanceof Player player) {
//            characterService.assignEntityMovementSpeed(living);
        }
    }

    public void onResourceRegen(ResourceEvent.Regen event, Player player) {
        double amount = characterService.calcResourceRegen(attributeFacade.getAllAttributes(player));
        event.setRegenAmount(amount);
    }

}

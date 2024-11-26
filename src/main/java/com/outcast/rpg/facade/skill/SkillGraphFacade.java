package com.outcast.rpg.facade.skill;

import com.google.inject.Inject;
import com.outcast.rpg.api.character.skill.TargetedSkill;
import com.outcast.rpg.api.event.SkillCastEvent;
import com.outcast.rpg.api.character.skill.DescriptionArgument;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.character.Character;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.skill.SkillGraphConfig;
import com.outcast.rpg.facade.character.AttributeFacade;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.CharacterService;
import com.outcast.rpg.service.skill.SkillGraphService;
import com.outcast.rpg.util.ConversionUtil;
import com.outcast.rpg.util.setting.PluginLoader;
import com.udojava.evalex.Expression;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.javatuples.Pair;

import java.util.*;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SkillGraphFacade {

    private final TextComponent NEW_LINE = Component.newline();

    @Inject RPGConfig rpgConfig;
    @Inject SkillGraphConfig skillGraphConfig;

    @Inject AttributeFacade attributeFacade;

    @Inject CharacterService characterService;
    @Inject ExpressionService expressionService;
    @Inject SkillGraphService skillGraphService;

    public void sendMessageOnSkillCast(SkillCastEvent.Post event) {
        TextComponent message;

        if(event.getResult().getMessage() != Component.empty()) {
            message = event.getResult().getMessage();
        } else {
            String name;

            if(event.getUser() instanceof Player)
                name = event.getUser().getName();
            else
                name = event.getUser().getCustomName();

            message = Component.text(name).color(GOLD)
                    .append(Component.text(" cast ").color(DARK_GRAY))
                    .append(Component.text(event.getSkill().getName()).color(DARK_GREEN));
        }

        event.getUser().getNearbyEntities(rpgConfig.SKILL_MESSAGE_DISTANCE, rpgConfig.SKILL_MESSAGE_DISTANCE, rpgConfig.SKILL_MESSAGE_DISTANCE).forEach(living -> {
            if(living instanceof Player)
                living.sendMessage(message);
        });
    }

    public TextComponent renderAvailableSkill(Skill skill, Player source, boolean showCost) {
        TextComponent skillText = renderSkill(skill, source);

        Set<Skill> linkedSkills = skillGraphService.getLinkedSkills(Collections.singletonList(skill));

        if (linkedSkills.isEmpty())
            return skillText;

        TextComponent hoverText = Component.empty();
        List<Skill> skills = characterService.getOrCreateCharacter(source).getSkills();
        if(showCost) {
            Character c = characterService.getOrCreateCharacter(source);
            hoverText.append(NEW_LINE).append(NEW_LINE)
                    .append(Component.text("EXP to Unlock: ").color(DARK_GREEN))
                    .append(Component.text(skillGraphService.getCostForSkill(c, skill, skills).get()).color(GOLD));
        }
        hoverText.append(NEW_LINE).append(Component.text("Next Skills: ").color(DARK_GREEN));

        int i=0;
        for(Skill linkedSkill : linkedSkills) {
            i++;
            hoverText.append(renderSkill(linkedSkill, source));
            if(i < linkedSkills.size())
                hoverText.append(Component.text(", ").color(GOLD));
        }

        return skillText.hoverEvent(HoverEvent.showText(hoverText));
    }
    public TextComponent renderSkill(Skill skill, Player source) {
        TextComponent hoverText = Component.text(skill.getName()).color(GOLD).append(NEW_LINE);

        hoverText.append(skill.getDescription(source)).append(NEW_LINE);
        hoverText.append(NEW_LINE).append(Component.text("Cooldown: ").color(DARK_GREEN)).append(Component.text(ConversionUtil.formatDuration(skill.getCooldown(source))).color(GOLD));
        hoverText.append(NEW_LINE).append(Component.text("Mana: ").color(DARK_GREEN)).append(Component.text(skill.getResource(source)).color(GOLD));

        if(skill instanceof TargetedSkill) {
            int range = (int) (((TargetedSkill) skill).getRange(source));
            hoverText.append(NEW_LINE).append(Component.text("Range: ").color(DARK_GREEN)).append(Component.text(range).color(GOLD));
        }

        return Component.text(skill.getName()).color(GOLD).hoverEvent(HoverEvent.showText(hoverText));
    }

    @SafeVarargs
    public final TextComponent renderSkillDescription(LivingEntity living, TextComponent textComponent, Pair<String, ?>... descriptionArgs) {

        if(descriptionArgs == null)
            return textComponent;

        for(Pair<String, ?> arg : descriptionArgs) {
            TextComponent value = Component.text(arg.getValue1().toString());

            if(arg.getValue1() instanceof DescriptionArgument)
                value = ((DescriptionArgument) arg.getValue1()).apply(living);

            if(arg.getValue1() instanceof Expression)
                value = Component.text(expressionService.evalExpression(living, (Expression) arg.getValue1()).toString());

        }

        return textComponent;
    }

    public long getSkillCooldown(LivingEntity living, String cooldownExpression) {
        return expressionService.evalExpression(living, cooldownExpression).longValue();
    }
    public double getSkillResourceCost(LivingEntity living, String resourceCostExpression) {
        return expressionService.evalExpression(living, resourceCostExpression).doubleValue();
    }
    public Optional<Skill> getSkillById(String skillId) {
        return PluginLoader.getSkillService().getSkillById(skillId)
                .filter(skill -> skill instanceof Skill)
                .map(skill -> (Skill) skill);
    }

}

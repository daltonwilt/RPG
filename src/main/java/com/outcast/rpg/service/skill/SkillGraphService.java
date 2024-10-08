package com.outcast.rpg.service.skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.RPG;
import com.outcast.rpg.api.character.skill.Skill;
import com.outcast.rpg.api.character.skill.SkillGraph;
import com.outcast.rpg.character.Character;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.skill.SkillGraphConfig;
import com.outcast.rpg.config.skill.SkillNodeConfig;
import com.outcast.rpg.facade.skill.SkillGraphFacade;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.util.exception.RPGCommandException;
import com.outcast.rpg.util.math.Graph;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class SkillGraphService {

    @Inject RPGConfig rpgConfig;
    @Inject SkillGraphConfig skillGraphConfig;

    @Inject SkillGraphFacade skillGraphFacade;

    @Inject AttributeService attributeService;
    @Inject ExpressionService expressionService;

    private SkillGraph skillGraph;
    private Set<Skill> uniqueSkills;

    public SkillGraph getSkillGraph() {
        if(skillGraph != null)
            return skillGraph;

        this.skillGraph = loadSkillGraph();
        return skillGraph;
    }

    private SkillGraph loadSkillGraph() {
        Map<String, Skill> namedSkillNodes = new HashMap<>();

        Skill rootSkill = getSkillFromConfigNode(skillGraphConfig.ROOT);
        namedSkillNodes.put("root-skill", rootSkill);

        // Create an empty skill graph
        SkillGraph rootGraph = new SkillGraph(rootSkill);

        // Read skills and store for later use
        skillGraphConfig.NODES.forEach((key, node) -> {
            namedSkillNodes.put(key, getSkillFromConfigNode(node));
        });

        // Read links between skills and apply to skill graph from previously stored interpreted skills
        skillGraphConfig.LINKS.forEach(skillNodeLinkConfig -> {
            Skill parent = namedSkillNodes.get(skillNodeLinkConfig.PARENT_SKILL_NODE_ID);
            Skill child = namedSkillNodes.get(skillNodeLinkConfig.CHILD_SKILL_NODE_ID);
            String cost = skillNodeLinkConfig.COST;
            Graph.LinkType type = Graph.LinkType.valueOf(skillNodeLinkConfig.TYPE.toString());

            rootGraph.add(parent, child, cost, type);
        });

        this.uniqueSkills = skillGraphConfig.UNIQUE_SKILLS.stream()
                .map(skillId -> skillGraphFacade.getSkillById(skillId).get())
                .collect(Collectors.toSet());

        return rootGraph;
    }

    public Set<Skill> getUniqueSkills() {
        return uniqueSkills;
    }

    public void resetSkillGraph() throws RPGCommandException {
        SkillGraph resetSkillGraph = resetSkillGraph = loadSkillGraph();

        if(!resetSkillGraph.equals(skillGraph)) {
            throw new RPGCommandException(
                    "The skill tree layout was changed. Changes will not be applied. (A full restart is required to change the tree's layout.)"
            );
        }

        this.skillGraph = resetSkillGraph;
    }

    private Skill getSkillFromConfigNode(SkillNodeConfig node) {
        Optional<Skill> skillOptional = skillGraphFacade.getSkillById(node.SKILL_ID);

        // If no castable object registered under the provided id could be found, throw an exception
        if (!skillOptional.isPresent())
            throw new NoSuchElementException("No registered skill with an id of '" + node.SKILL_ID + "' could not be found.");

        Skill skill = skillOptional.get();

        // Set configured cooldown expression, if available
        if (node.COOLDOWN_EXPRESSION != null)
            skill.setCooldownExpression(node.COOLDOWN_EXPRESSION);

        // Set configured cost expression, if available
        if (node.COST_EXPRESSION != null)
            skill.setResourceCostExpression(node.COST_EXPRESSION);

        // Set configured properties, if available
        if (node.PROPERTIES != null && !node.PROPERTIES.isEmpty())
            skill.setProperties(node.PROPERTIES);

        RPG.warn(skill.toString());
        return skill;
    }

    public Skill getSkillGraphRoot() {
        return getSkillGraph().getRoot();
    }

    /**
     * Tests whether a list of skill ID's has a path through the server's skill graph
     * @return Whether the list passes
     */
    public boolean isPathValid(List<Skill> skills) {
        if(skills.isEmpty())
            return false;

        if(!getSkillGraph().getRoot().equals(skills.get(0)))
            return false;

        int i = 1;
        for(Skill skill : skills.subList(1, skills.size())) {
            if(!checkLink(skill, skills.subList(0, i)))
                return false;
            i++;
        }

        return true;
    }

    private boolean checkLink(Skill skill, List<Skill> previousSkills) {
        for (Skill otherSkill : previousSkills) {
            if (getSkillGraph().getLink(skill, otherSkill) != null) {
                return true;
            }
        }
        return false;
    }

    public Set<Skill> getLinkedSkills(List<Skill> skills) {
        Set<Skill> linked = new HashSet<>();

        boolean hasUnique = skills.stream()
                .anyMatch(skill -> uniqueSkills.contains(skill));

        skills.forEach(s -> {
            getSkillGraph().getLinksWhereParent(s).forEach(link -> {
                Skill child = link.getChild().getData();
                Skill parent = link.getParent().getData();

                // If the link is bidirectional we have to check if the other node is the "parent"
                if (link.getType() == Graph.LinkType.BIDIRECTIONAL) {
                    if (!skills.contains(parent) && (!hasUnique || !uniqueSkills.contains(parent))) {
                        linked.add(parent);
                    }
                }
                if (!skills.contains(child) && (!hasUnique || !uniqueSkills.contains(child))) {
                    linked.add(child);
                }
            });
        });

        return linked;
    }

    public Optional<Graph.Link<Skill>> getLinkBetween(Skill skill, List<Skill> skillIds) {
        for (Skill other : skillIds) {
            Graph.Link<Skill> link = getSkillGraph().getLink(skill, other);
            if (link != null) {
                return Optional.of(link);
            }
        }
        return Optional.empty();
    }

    public Optional<Double> getCostForSkill(Character c, Skill skill, List<Skill> skills) {
        return getLinkBetween(skill, skills).map(link -> {
            Expression expression = expressionService.getExpression(link.getWeight());
            expression.setVariable("SKILLS", BigDecimal.valueOf(skills.size()));
//            expression.setVariable("ATTRIBUTES", BigDecimal.valueOf(attributeService.getUpgradeableAttributeCount(c)));
            return expression.eval().doubleValue();
        });
    }

}

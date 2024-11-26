package com.outcast.rpg.api.skill;

public abstract class TargetedSkill extends AbstractSkill implements TargetedCastable{
    protected TargetedSkill(String id, String name) { super(id, name); }
}

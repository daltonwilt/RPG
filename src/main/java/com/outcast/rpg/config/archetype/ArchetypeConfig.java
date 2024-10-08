package com.outcast.rpg.config.archetype;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public class ArchetypeConfig {

    @Setting("display-name")
    public String NAME = "Example";

    @Setting("skills")
    public Set<String> SKILLS = new HashSet<>();

}

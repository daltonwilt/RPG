package com.outcast.rpg.util.setting;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.outcast.rpg.config.CoreConfig;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.RPGSkillsConfig;
import com.outcast.rpg.config.mob.MobsConfig;
import com.outcast.rpg.config.skill.SkillGraphConfig;
import com.outcast.rpg.facade.character.AttributeFacade;
import com.outcast.rpg.facade.character.CharacterFacade;
import com.outcast.rpg.facade.character.MobFacade;
import com.outcast.rpg.facade.character.ResourceFacade;
import com.outcast.rpg.facade.skill.SkillFacade;
import com.outcast.rpg.listener.EntityListener;
import com.outcast.rpg.listener.ResourceListener;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.service.character.CharacterService;
import com.outcast.rpg.service.character.ResourceService;
import com.outcast.rpg.service.skill.CooldownService;
import com.outcast.rpg.service.skill.SkillGraphService;
import com.outcast.rpg.service.skill.SkillService;

public class PluginModule extends AbstractModule {

    @Override
    protected void configure() {
        // Configs
        bind(CoreConfig.class).in(Scopes.SINGLETON);
        bind(RPGConfig.class).in(Scopes.SINGLETON);
        bind(RPGSkillsConfig.class).in(Scopes.SINGLETON);

        bind(MobsConfig.class).in(Scopes.SINGLETON);
        bind(SkillGraphConfig.class).in(Scopes.SINGLETON);

        // Repository
//        bind(CharacterRepository.class).in(Scopes.SINGLETON);

        // Services
        bind(AttributeService.class).in(Scopes.SINGLETON);
        bind(CharacterService.class).in(Scopes.SINGLETON);
        bind(CooldownService.class).in(Scopes.SINGLETON);
//        bind(DamageService.class).in(Scopes.SINGLETON);
        bind(ExpressionService.class).in(Scopes.SINGLETON);
        bind(ResourceService.class).in(Scopes.SINGLETON);
        bind(SkillService.class).in(Scopes.SINGLETON);

        // Facades
        bind(AttributeFacade.class).in(Scopes.SINGLETON);
        bind(CharacterFacade.class).in(Scopes.SINGLETON);
        bind(MobFacade.class).in(Scopes.SINGLETON);
        bind(ResourceFacade.class).in(Scopes.SINGLETON);
        bind(SkillFacade.class).in(Scopes.SINGLETON);

        // Listeners
        bind(EntityListener.class).in(Scopes.SINGLETON);
        bind(ResourceListener.class).in(Scopes.SINGLETON);
//        bind(SkillsListener.class).in(Scopes.SINGLETON);
    }

}

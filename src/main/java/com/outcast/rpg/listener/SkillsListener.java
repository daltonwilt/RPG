package com.outcast.rpg.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.event.ResourceEvent;
import com.outcast.rpg.api.event.SkillCastEvent;
import com.outcast.rpg.facade.character.CharacterFacade;
import com.outcast.rpg.facade.skill.SkillGraphFacade;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
public class SkillsListener implements Listener {

    @Inject
    private CharacterFacade characterFacade;

    @Inject
    private SkillGraphFacade skillGraphFacade;

    @EventHandler
    public void onResourceRegen(ResourceEvent.Regen event) {
        if(event.getEntity().get() instanceof  Player player) {
            characterFacade.onResourceRegen(event, player);
        }
    }

    public void onSkillCast(SkillCastEvent.Post event) {
        skillGraphFacade.sendMessageOnSkillCast(event);
    }

}

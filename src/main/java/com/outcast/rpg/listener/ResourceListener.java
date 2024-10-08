package com.outcast.rpg.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.event.ResourceEvent;
import com.outcast.rpg.facade.character.ResourceFacade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
public class ResourceListener implements Listener {

    @Inject
    private ResourceFacade resourceFacade;

    @EventHandler
    public void onResourceRegen(ResourceEvent.Regen event) {
        resourceFacade.onResourceRegen(event);
    }

}

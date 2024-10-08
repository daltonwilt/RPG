package com.outcast.rpg.facade.character;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpg.api.character.ResourceUser;
import com.outcast.rpg.api.event.ResourceEvent;
import com.outcast.rpg.character.EntityResourceUser;
import com.outcast.rpg.config.RPGSkillsConfig;
import com.outcast.rpg.service.character.ResourceService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

@Singleton
public class ResourceFacade {

    @Inject private RPGSkillsConfig skillsConfig;
    @Inject private ResourceService resourceService;

    private ImmutableMap<Integer, TextComponent> BARS;

    @Inject
    public ResourceFacade(ResourceService resourceService, RPGSkillsConfig skillsConfig) {
        if(resourceService != null && skillsConfig != null) {
            this.resourceService = resourceService;
            this.skillsConfig = skillsConfig;

            BARS = ImmutableMap.<Integer, TextComponent>builder()
                    .put(0, Component.text("▉▉▉▉▉▉▉▉▉▉").color(NamedTextColor.DARK_GRAY))
                    .put(1, Component.text("▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉▉▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(2, Component.text("▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(3, Component.text("▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(4, Component.text("▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(5, Component.text("▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(6, Component.text("▉▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(7, Component.text("▉▉▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(8, Component.text("▉▉▉▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉▉").color(NamedTextColor.DARK_GRAY)))
                    .put(9, Component.text("▉▉▉▉▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR).append(Component.text("▉").color(NamedTextColor.DARK_GRAY)))
                    .put(10, Component.text("▉▉▉▉▉▉▉▉▉▉").color(skillsConfig.RESOURCE_COLOR))
                    .build();
        }
    }

    public void onResourceRegen(ResourceEvent.Regen event) {
        if(event.getRegenAmount() > 0 && event.getResourceUser() instanceof EntityResourceUser user) {
            Player player = Bukkit.getServer().getPlayer(user.getId());

            ResourceUser rUser = event.getResourceUser();

            if(player != null) {

                int amount = (int) (event.getRegenAmount() + rUser.getCurrent());
                amount = amount < rUser.getMax() ? amount : (int) rUser.getMax();
                int bars = Math.max(((int) ((amount / rUser.getMax()) * 10)), 0);

                TextComponent display = Objects.requireNonNull(BARS.get(bars)).append(Component.text(" " + amount + "/" + (int) rUser.getMax() + " " + skillsConfig.RESOURCE_NAME));

                player.sendActionBar(display);
            }
        }
    }

    public void onPlayerJoin(Player player) {
        resourceService.getOrCreateUser(player);
    }

}

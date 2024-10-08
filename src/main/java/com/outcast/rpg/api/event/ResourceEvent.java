package com.outcast.rpg.api.event;

import com.outcast.rpg.api.character.ResourceUser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ResourceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    boolean isCanceled = false;
    private LivingEntity living;
    private final ResourceUser user;

    public ResourceEvent(LivingEntity living, ResourceUser user) {
        this.living = living;
        this.user = user;
    }

    public ResourceEvent(ResourceUser user) {
        this.user = user;
    }

    public ResourceUser getResourceUser() {
        return user;
    }

    public Optional<LivingEntity> getEntity() {
        return  Optional.ofNullable(living);
    }

    public static class Create extends  ResourceEvent {
        public Create(LivingEntity living, ResourceUser user) {
            super(living, user);
        }

        public Create(ResourceUser user) {
            super(user);
        }
    }

    @Setter
    @Getter
    public static class Regen extends ResourceEvent implements Cancellable {
        private double regenAmount;

        public Regen(LivingEntity living, ResourceUser user, double regenAmount) {
            super(living, user);
            this.regenAmount = regenAmount;
        }

        public Regen(ResourceUser user, double regenAmount) {
            super(user);
            this.regenAmount = regenAmount;
        }

    }

    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCanceled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
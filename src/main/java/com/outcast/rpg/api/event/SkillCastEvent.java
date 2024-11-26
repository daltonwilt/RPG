package com.outcast.rpg.api.event;

import com.outcast.rpg.api.skill.CastResult;
import com.outcast.rpg.api.skill.Castable;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkillCastEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    boolean isCanceled = false;
    @Getter
    protected LivingEntity user;
    @Getter
    protected Castable skill;
    @Getter
    private final long timestamp;

    public SkillCastEvent(LivingEntity living, Castable skill, long timestamp) {
        this.user = living;
        this.skill = skill;
        this.timestamp = timestamp;
    }

    public static class Pre extends SkillCastEvent implements Cancellable {
        private boolean cancelled;

        public Pre(LivingEntity user, Castable skill, long timestamp) {
            super(user, skill, timestamp);
        }

        public void setUser(LivingEntity user) {
            this.user = user;
        }

        public void setSkill(Castable skill) {
            this.skill = skill;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }
    }

    @Getter
    public static class Post extends SkillCastEvent {
        private final CastResult result;

        public Post(LivingEntity user, Castable skill, long timestamp, CastResult result) {
            super(user, skill, timestamp);
            this.result = result;
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

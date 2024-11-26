package com.outcast.rpg.api.event.db;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HibernateConfigurationEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private List<Class<?>> classes;

    boolean isCanceled = false;

    public HibernateConfigurationEvent(List<Class<?>> classes) {
        this.classes = classes;
    }

    public void registerEntity(Class<?> clazz) {
        this.classes.add(clazz);
    }
    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        this.isCanceled = canceled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
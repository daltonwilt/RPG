package com.outcast.rpg.api.event.db;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMigrationEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final List<String> pluginIds = new ArrayList<>();

    boolean isCanceled = false;

    public DatabaseMigrationEvent() {}

    public void registerForMigration(String pluginId) {
        this.pluginIds.add(pluginId);
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

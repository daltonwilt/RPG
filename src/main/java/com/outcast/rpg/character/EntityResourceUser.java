package com.outcast.rpg.character;

import com.outcast.rpg.api.character.ResourceUser;
import com.outcast.rpg.db.SpigotIdentifiable;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.UUID;

public class EntityResourceUser implements SpigotIdentifiable, ResourceUser {

    private final UUID uuid;
    private double current;
    private double max;

    public EntityResourceUser(LivingEntity entity) {
        this.uuid = entity.getUniqueId();
    }

    @Nonnull
    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public void fill(double amount) {
        this.current += amount;
    }

    @Override
    public void fill() {
        this.current = max;
    }

    @Override
    public void drain(double amount) {
        this.current -= amount;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public void setMax(double amount) {
        this.max = amount;
    }

    @Override
    public double getCurrent() {
        return current;
    }

}

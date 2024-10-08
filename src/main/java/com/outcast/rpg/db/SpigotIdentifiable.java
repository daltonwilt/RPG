package com.outcast.rpg.db;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface SpigotIdentifiable extends Identifiable<UUID> {
    @Nonnull
    default UUID getUniqueId() {
        return getId();
    }
}

package com.outcast.rpg.db.migration;

import com.outcast.rpg.RPG;
import com.outcast.rpg.db.JPAConfig;
import com.outcast.rpg.api.event.db.DatabaseMigrationEvent;
import org.bukkit.Bukkit;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class DatabaseMigrator {

    private JPAConfig config;

    public DatabaseMigrator(JPAConfig config) {
        this.config = config;
    }

    public void migrate() {
        RPG.info("Beginning database migration...");

        String vendor = config.HIBERNATE.get(JPAConfig.URL_KEY).split(":")[1];

        DatabaseMigrationEvent event = new DatabaseMigrationEvent();
        Bukkit.getPluginManager().callEvent(event);

        event.getPluginIds().forEach(pluginId -> {
            String location = String.format("classpath:db/migration/%s/%s", pluginId, vendor);
            RPG.info("Migrating %s", location);

            FluentConfiguration cfg = new FluentConfiguration()
                    .dataSource(
                            config.HIBERNATE.get(JPAConfig.URL_KEY),
                            config.HIBERNATE.get(JPAConfig.USERNAME_KEY),
                            config.HIBERNATE.get(JPAConfig.PASSWORD_KEY)
                    )
                    .schemas(pluginId)
                    .table("flyway_schema_history_" + pluginId)
                    .locations(location);

            new Flyway(cfg).migrate();
        });

        RPG.info("Database migration complete.");
    }

}

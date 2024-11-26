package com.outcast.rpg.config;

import com.google.inject.Singleton;
import com.outcast.rpg.db.JPAConfig;
import com.outcast.rpg.util.setting.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Singleton
public class CoreConfig extends PluginConfig {

    @Setting("combat-time")
    public Duration COMBAT_TIME = Duration.of(30, ChronoUnit.SECONDS);

    @Setting("db-enabled")
    public boolean DB_ENABLED = false;

    @Setting("jpa")
    public JPAConfig JPA_CONFIG = new JPAConfig();

    public CoreConfig() throws IOException {
        super("plugins/rpg", "core-config.conf");
    }

}

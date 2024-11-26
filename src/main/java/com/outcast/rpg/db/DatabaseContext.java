package com.outcast.rpg.db;

import com.outcast.rpg.RPG;
import com.outcast.rpg.db.migration.DatabaseMigrator;
import com.outcast.rpg.api.event.db.HibernateConfigurationEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import jakarta.persistence.EntityManagerFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DatabaseContext implements AutoCloseable {

    private final JPAConfig config;

    @Getter
    private EntityManagerFactory entityManagerFactory;

    public DatabaseContext(JPAConfig config) {
        this.config = config;

        DatabaseMigrator migrator = new DatabaseMigrator(config);
        migrator.migrate();

        createEntityManagerFactory();
    }

    private void createEntityManagerFactory() {
        MetadataSources metadataSources = new MetadataSources(configureServiceRegistry(config));

        addClasses(metadataSources);

        try {
            entityManagerFactory = metadataSources.buildMetadata()
                    .getSessionFactoryBuilder()
                    .build();
        } catch(Exception e) {
            RPG.severe("Cause :: %s", e.getCause());
            RPG.severe("Error :: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    private ServiceRegistry configureServiceRegistry(JPAConfig config) {
        return new StandardServiceRegistryBuilder()
                .applySettings(getProperties(config))
                .build();
    }

    private Properties getProperties(JPAConfig config) {
        Properties properties = new Properties();

        config.HIBERNATE.forEach(properties::setProperty);

        return properties;
    }

    private void addClasses(MetadataSources metadataSources) {
        List<Class<?>> classes = new LinkedList<>();

        HibernateConfigurationEvent event = new HibernateConfigurationEvent(classes);
        Bukkit.getPluginManager().callEvent(event);

        classes.forEach(metadataSources::addAnnotatedClass);
    }

    @Override
    public void close() {
        if (entityManagerFactory != null ) {
            entityManagerFactory.close();
        }
    }

}
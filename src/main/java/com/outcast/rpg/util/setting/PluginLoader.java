package com.outcast.rpg.util.setting;

import com.google.common.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.outcast.rpg.character.attribute.AttributeType;
import com.outcast.rpg.character.attribute.AttributeTypeRegistry;
import com.outcast.rpg.skills.BlankSkill;
import com.outcast.rpg.command.CommandService;
import com.outcast.rpg.config.CoreConfig;
import com.outcast.rpg.config.RPGConfig;
import com.outcast.rpg.config.RPGSkillsConfig;
import com.outcast.rpg.config.archetype.ArchetypesConfig;
import com.outcast.rpg.config.attribute.AttributesConfig;
import com.outcast.rpg.config.mob.MobsConfig;
import com.outcast.rpg.config.skill.SkillGraphConfig;
import com.outcast.rpg.db.DatabaseContext;
import com.outcast.rpg.facade.character.AttributeFacade;
import com.outcast.rpg.facade.character.CharacterFacade;
import com.outcast.rpg.facade.character.MobFacade;
import com.outcast.rpg.facade.character.ResourceFacade;
import com.outcast.rpg.facade.skill.SkillFacade;
import com.outcast.rpg.facade.skill.SkillGraphFacade;
import com.outcast.rpg.listener.EntityListener;
import com.outcast.rpg.listener.ResourceListener;
import com.outcast.rpg.service.ExpressionService;
import com.outcast.rpg.service.character.AttributeService;
import com.outcast.rpg.service.character.CharacterService;
import com.outcast.rpg.service.character.MobService;
import com.outcast.rpg.service.character.ResourceService;
import com.outcast.rpg.service.skill.CooldownService;
import com.outcast.rpg.service.skill.SkillGraphService;
import com.outcast.rpg.service.skill.SkillService;
import com.outcast.rpg.skills.Fireball;
import com.outcast.rpg.util.serialize.AttributeTypeSerializer;
import com.outcast.rpg.util.serialize.TextColorTypeSerializer;
import com.outcast.rpg.util.serialize.DurationTypeSerializer;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class PluginLoader {

    @Inject
    private static Injector spigotInjector;
    private static Injector rpgInjector;

    private static Economy economy;
    private static Permission permission;

    @Getter
    private static DatabaseContext databaseContext;
    private static Components components;

    @Getter
    private static BukkitAudiences adventure;

    public static void load(JavaPlugin plugin) {
        // Register Serialization/Deserialization Types
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Duration.class), new DurationTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(TextColor.class), new TextColorTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(AttributeType.class), new AttributeTypeSerializer());

        // Inject Components
        components = new Components();
        spigotInjector = Guice.createInjector();
        rpgInjector = spigotInjector.createChildInjector(new PluginModule());
        rpgInjector.injectMembers(components);

        // Initialize Database
        getCoreConfig().init();
        if (getCoreConfig().DB_ENABLED)
            databaseContext = new DatabaseContext(getCoreConfig().JPA_CONFIG);

        // Initialize Registries
        new AttributeTypeRegistry();

        // Register Skills
        components.skillService.registerSkills(
                new Fireball(),
                new BlankSkill("root-skill", "RootSkill")
        );

        // Initialize Configs
        getRPGConfig().init();
        getRPGSkillsConfig().init();

        getArchetypesConfig().init();
        getMobsConfig().init();
        getSkillGraphConfig().init();

        /// Initialize Services
        getCharacterService().init();
        getExpressionService().init();
    }

    public static void setup(JavaPlugin plugin) {
        // Register Listeners
        plugin.getServer().getPluginManager().registerEvents(components.entityListener, plugin);
        plugin.getServer().getPluginManager().registerEvents(components.resourceListener, plugin);

        // Register Commands
        CommandService.registerCommands();

        // initialize cache
        // initialize services
        // healing
        getResourceService().init();

        adventure = BukkitAudiences.create(plugin);
    }

    public static void disable(JavaPlugin plugin) {
        adventure.close();
    }

    public Injector getRpgInjector() {
        return rpgInjector;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return getDatabaseContext().getEntityManagerFactory();
    }

    public static CoreConfig getCoreConfig() {
        return components.coreConfig;
    }
    public static RPGConfig getRPGConfig() {
        return components.rpgConfig;
    }
    public static RPGSkillsConfig getRPGSkillsConfig() {
        return components.rpgSkillsConfig;
    }

    public static ArchetypesConfig getArchetypesConfig() {
        return components.archetypesConfig;
    }
    public static AttributesConfig getAttributesConfig() {
        return components.attributesConfig;
    }
    public static MobsConfig getMobsConfig() {
        return components.mobsConfig;
    }
    public static SkillGraphConfig getSkillGraphConfig() {
        return components.skillGraphConfig;
    }

    public static AttributeService getAttributeService() {
        return components.attributeService;
    }
    public static CharacterService getCharacterService() {
        return components.characterService;
    }
    public static CooldownService getCooldownService() {
        return components.cooldownService;
    }
    public static ExpressionService getExpressionService() {
        return components.expressionService;
    }
    public static MobService getMobService() {
        return components.mobService;
    }
    public static ResourceService getResourceService() {
        return components.resourceService;
    }
    public static SkillService getSkillService() {
        return components.skillService;
    }
    public static SkillGraphService getSkillGraphService() {
        return components.skillGraphService;
    }

    public  static AttributeFacade getAttributeFacade() { return components.attributeFacade; }
    public static CharacterFacade getCharacterFacade() { return components.characterFacade; }
    public static MobFacade getMobFacade() { return components.mobFacade; }
    public static ResourceFacade getResourceFacade() { return components.resourceFacade; }
    public static SkillFacade getSkillFacade() { return components.skillFacade; }
    public static SkillGraphFacade getSkillGraphFacade() { return components.skillGraphFacade; }

    private static class Components {
        // Configs
        @Inject CoreConfig coreConfig;
        @Inject RPGConfig rpgConfig;
        @Inject RPGSkillsConfig rpgSkillsConfig;

        @Inject ArchetypesConfig archetypesConfig;
        @Inject AttributesConfig attributesConfig;
        @Inject MobsConfig mobsConfig;
        @Inject SkillGraphConfig skillGraphConfig;
        // Item Template Config

        // Repository
//        @Inject CharacterRepository characterRepository;

        // Service
        @Inject AttributeService attributeService;
        @Inject CharacterService characterService;
        @Inject CooldownService cooldownService;
        // Damage Service
        @Inject ExpressionService expressionService;
        // Healing Service
        @Inject MobService mobService;
        @Inject ResourceService resourceService;
        @Inject SkillService skillService;
        @Inject SkillGraphService skillGraphService;

        // Facade
        @Inject AttributeFacade attributeFacade;
        @Inject CharacterFacade characterFacade;
        // Item Facade
        @Inject MobFacade mobFacade;
        @Inject ResourceFacade resourceFacade;
        @Inject SkillFacade skillFacade;
        @Inject SkillGraphFacade skillGraphFacade;

        // Listener
        @Inject EntityListener entityListener;
        @Inject ResourceListener resourceListener;
        // Skills Listener
    }

}

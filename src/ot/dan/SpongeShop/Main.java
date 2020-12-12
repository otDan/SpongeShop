package ot.dan.SpongeShop;

import com.erigitic.main.TotalEconomy;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Commands.*;
import ot.dan.SpongeShop.Guis.CategoryGUI;
import ot.dan.SpongeShop.Objects.CategoryManager;
import ot.dan.SpongeShop.Objects.ConfigManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(id = "spongeshop", name = "Sponge Shop", version = "1.0", description = "Shop plugin made for Sponge")
public class Main {
    @Inject
    private Logger logger;
    @Inject
    private PluginManager pluginManager;
    private PluginContainer pluginContainer;
    private CategoryGUI categoryGUI;

    public Logger getLogger() {
        return logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private ConfigManager configManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Optional<PluginContainer> pluginContainer = this.pluginManager.getPlugin("spongeshop");
        pluginContainer.ifPresent(container -> this.pluginContainer = container);

        //Grab Instance of Config Manager
        configManager = ConfigManager.getInstance();

        //Alternative method of setup using Lambda Expressions
        configManager.setup(defaultConfig, configLoader, getLogger(), config -> {
            config.getNode("donottouch").setComment("Important do not touch").setValue(true);
        });
        configManager.saveConfig();

        new CategoryManager(configManager, logger, this.pluginContainer);
        CategoryManager.init();
        initEvents();
        initGuis();
        initCommands();
        logger.info("Successfully running SpongeShop");
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        CategoryManager.save();
    }

    private void initEvents() {

    }

    private void initGuis() {
        categoryGUI = new CategoryGUI(this.pluginContainer);
        categoryGUI.initialize();
    }

    private void initCommands() {
        CommandSpec category = CommandSpec.builder()
                .description(Text.of(""))
                .arguments(GenericArguments.string(Text.of("category")), GenericArguments.integer(Text.of("slot")), GenericArguments.integer(Text.of("buyprice")), GenericArguments.integer(Text.of("sellprice")))
                .permission("spongeshop.admin")
                .executor(new ShopCategorySetCMD())
                .build();

        CommandSpec slot = CommandSpec.builder()
                .description(Text.of(""))
                .arguments(GenericArguments.integer(Text.of("slot")), GenericArguments.string(Text.of("category")))
                .permission("spongeshop.admin")
                .executor(new ShopSetCMD(this.categoryGUI))
                .build();

        CommandSpec help = CommandSpec.builder()
                .description(Text.of(""))
                .permission("spongeshop.admin")
                .executor(new ShopHelpCMD())
                .build();

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .description(Text.of("Sponge Shop Command"))
                .permission("spongeshop.use")
                .executor(new ShopCMD(this.categoryGUI))
                .child(slot, "set")
                .child(category, "category")
                .child(help, "help")
                .build(), "spongeshop", "shops", "buy", "shop");

        CommandSpec delete = CommandSpec.builder()
                .description(Text.of(""))
                .arguments(GenericArguments.string(Text.of("category")))
                .permission("spongeshop.admin")
                .executor(new CategoryCMD(3))
                .build();

        CommandSpec categories = CommandSpec.builder()
                .description(Text.of(""))
                .permission("spongeshop.admin")
                .executor(new CategoryCMD(2))
                .build();

        CommandSpec create = CommandSpec.builder()
                .description(Text.of(""))
                .arguments(GenericArguments.string(Text.of("category")))
                .permission("spongeshop.admin")
                .executor(new CategoryCMD(1))
                .build();

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .description(Text.of("Category Command"))
                .permission("spongeshop.admin")
                .executor(new CategoryCMD(0))
                .child(create, "create", "add")
                .child(delete, "delete", "remove")
                .child(categories, "list")
                .build(), "category", "categories", "cat", "catego");
    }
}

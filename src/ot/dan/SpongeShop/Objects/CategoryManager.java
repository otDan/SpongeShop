package ot.dan.SpongeShop.Objects;

import com.erigitic.main.TotalEconomy;
import com.google.common.reflect.TypeToken;
import inventory.Action;
import inventory.Element;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.Actions;
import ot.dan.SpongeShop.Guis.ShopGUI;
import java.util.*;
import java.util.function.Consumer;

public class CategoryManager {
    private static List<Category> categories;
    private static Category main;
    private static ConfigManager configManager;
    private static Logger logger;
    private static PluginContainer pluginContainer;

    public CategoryManager(ConfigManager configManager, Logger logger, PluginContainer pluginContainer) {
        CategoryManager.configManager = configManager;
        CategoryManager.logger = logger;
        CategoryManager.pluginContainer = pluginContainer;
    }

    public static Category getCategory(String name) {
        for (Category category:categories) {
            if(category.getName() != null) {
                if (category.getName().equalsIgnoreCase(name)) {
                    return category;
                }
            }
        }
        return null;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static void setConfigManager(ConfigManager configManager) {
        CategoryManager.configManager = configManager;
    }

    public static void init() {
        boolean foundMain = false;
        categories = new ArrayList<>();

        Consumer<Action.Click> clickActionMain = click -> {
            ItemStack itemStack = click.getElement().getItem().createStack();
            Optional<List<Text>> lore = itemStack.get(Keys.ITEM_LORE);
            if(lore.isPresent()) {
                if(lore.get().size() == 4) {
                    Text text = Text.of(lore.get().get(3));
                    Category cat = CategoryManager.getCategory(text.toPlain());
                    if(cat != null) {
                        ShopGUI shopGUI = new ShopGUI(pluginContainer, cat);
                        shopGUI.getPage().open(click.getPlayer(), 0);
                    }
                }
            }
        };

        CommentedConfigurationNode config = configManager.getConfig();
        for (ConfigurationNode node: config.getNode("shops").getChildrenMap().values()) {
            String category = node.getNode("name").getString();
            if(category != null) {
                if (category.equalsIgnoreCase("main")) {
                    List<Element> elements = new ArrayList<>();
                    for (int i = 0; i < node.getChildrenMap().size() - 1; i++) {
                        elements.add(Element.EMPTY);
                    }
                    for (ConfigurationNode item : node.getChildrenMap().values()) {
                        ItemStack itemStack = null;
                        try {
                            itemStack = item.getNode("item").getValue(TypeToken.of(ItemStack.class));
                        } catch (ObjectMappingException ignored) {}

                        int slot = item.getNode("slot").getInt();
                        if (itemStack != null) {
                            Optional<List<Text>> lore = itemStack.get(Keys.ITEM_LORE);

                            if (lore.isPresent()) {
                                if (lore.get().size() > 0) {
                                    elements.set(slot, Element.of(itemStack, clickActionMain));
                                } else {
                                    elements.set(slot, Element.of(itemStack));
                                }
                            } else {
                                elements.set(slot, Element.of(itemStack));
                            }

                        } else {
                            logger.info("This item is null!");
                        }
                    }
                    main = new Category(category, elements);
                    foundMain = true;
                } else {
                    List<Element> elements = new ArrayList<>();
                    for (int i = 0; i < node.getChildrenMap().size() - 1; i++) {
                        elements.add(Element.EMPTY);
                    }
                    for (ConfigurationNode item : node.getChildrenMap().values()) {

                        ItemStack itemStack = null;
                        try {
                            itemStack = item.getNode("item").getValue(TypeToken.of(ItemStack.class));
                        } catch (ObjectMappingException ignored) {}


                        int slot = item.getNode("slot").getInt();
                        if (itemStack != null) {
                            Optional<List<Text>> lore = itemStack.get(Keys.ITEM_LORE);
                            if(lore.isPresent()) {
                                if (lore.get().size() > 0) {
                                    elements.set(slot, Element.of(itemStack, Actions.getClickAction()));
                                } else {
                                    elements.set(slot, Element.of(itemStack));
                                }
                            }
                            else {
                                elements.set(slot, Element.of(itemStack));
                            }
                        } else {
                            logger.info("This item is null!");
                        }
                    }
                    categories.add(new Category(category, elements));
                }
            }
        }

        if(!foundMain) {
            main = new Category("main");
        }

    }

    public static void save() {
        CommentedConfigurationNode config = configManager.getConfig();
        config.getNode("shops").setValue(null);
        ConfigurationNode node = config.getNode("shops");
        int iv = 0;
        String i = "i" + iv;
        for (Category category:categories) {
            node.getNode(i).getNode("name").setValue(category.getName());
            int jv = 0;
            String j = "j" + jv;
            for (Element element:category.getElements()) {
                ItemStackSnapshot item = element.getItem();
                try {
                    node.getNode(i).getNode(j).getNode("item").setValue(TypeToken.of(ItemStack.class), item.createStack());
                } catch (ObjectMappingException ignored) {}

                node.getNode(i).getNode(j).getNode("slot").setValue(jv);

                Optional<List<Text>> lore = item.get(Keys.ITEM_LORE);
                if(lore.isPresent()) {
                    boolean buy = false;
                    boolean sell = false;
                    for (Text text:lore.get()) {
                        if(text.toPlain().toLowerCase().contains("buy")) {
                            node.getNode(i).getNode(j).getNode("buy").setValue(Integer.parseInt(text.toPlain().split("\\$")[1]));
                            buy = true;
                        }
                        if(text.toPlain().toLowerCase().contains("sell")) {
                            node.getNode(i).getNode(j).getNode("sell").setValue(Integer.parseInt(text.toPlain().split("\\$")[1]));
                            sell = true;
                        }
                    }
                    if(!buy) {
                        node.getNode(i).getNode(j).getNode("buy").setValue(-1);
                    }
                    if(!sell) {
                        node.getNode(i).getNode(j).getNode("sell").setValue(-1);
                    }
                }
                else {
                    node.getNode(i).getNode(j).getNode("buy").setValue(-1);
                    node.getNode(i).getNode(j).getNode("sell").setValue(-1);
                }
                jv++;
                j = "j" + jv;
            }
            iv++;
            i = "i" + iv;
        }

        configManager.saveConfig();

        node.getNode("im").getNode("name").setValue("main");
        int j = 0;
        for (Element element:main.getElements()) {
            ItemStackSnapshot item = element.getItem();
            try {
                node.getNode("im").getNode("jm" + j).getNode("item").setValue(TypeToken.of(ItemStack.class), item.createStack());
            } catch (ObjectMappingException ignored) {}
            node.getNode("im").getNode("jm" + j).getNode("slot").setValue(j);


            Optional<List<Text>> lore = item.get(Keys.ITEM_LORE);
            if(lore.isPresent()) {
                if(lore.get().size() == 4) {
                    String categoryName = lore.get().get(3).toPlain();
                    node.getNode("im").getNode("jm" + j).getNode("category").setValue(categoryName);
                }
            }
            else {
                node.getNode("im").getNode("jm" + j).getNode("category").setValue("nope");
            }
            j++;
        }


        configManager.saveConfig();
    }

    public static Category getMain() {
        return main;
    }

    public static List<Category> getCategories() {
        return categories;
    }

    public static void setCategories(List<Category> categories) {
        CategoryManager.categories = categories;
    }
}

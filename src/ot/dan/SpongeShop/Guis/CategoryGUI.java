package ot.dan.SpongeShop.Guis;

import inventory.Action;
import inventory.Element;
import inventory.Layout;
import inventory.Page;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Objects.Category;
import ot.dan.SpongeShop.Objects.CategoryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CategoryGUI {
    private final PluginContainer pluginContainer;
    private Page page;

    public CategoryGUI(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;

        initialize();
    }

    public void initialize() {
        Layout layout = Layout.builder()
//                .set(Element.EMPTY, 46, 47, 51, 52)
                .set(Page.FIRST, 45)
                .set(Page.PREVIOUS, 48)
                .set(Page.CURRENT, 49)
                .set(Page.NEXT, 50)
                .set(Page.LAST, 53)
                .build();

        page = Page.builder().layout(layout).build(pluginContainer);

        page.define(CategoryManager.getMain().getElements());
    }

    public void setCategory(int slot, ItemStack passItem, String category) {
        Consumer<Action.Click> clickAction = click -> {
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

        List<Text> lore = new ArrayList<>();
        lore.add(Text.EMPTY);
        lore.add(Text.EMPTY);
        lore.add(Text.EMPTY);
        lore.add(Text.of("§0§k" + category));

        passItem.offer(Keys.ITEM_LORE, lore);

        if(slot >= CategoryManager.getMain().getElements().size()) {
            while (slot >= CategoryManager.getMain().getElements().size()) {
                for (int j = 0; j < 49; j++) {
                    CategoryManager.getMain().getElements().add(Element.of(ItemStack.of(ItemTypes.GLASS_PANE)));
                }
            }
        }

        CategoryManager.getMain().getElements().set(slot, Element.of(passItem, clickAction));
        page.define(CategoryManager.getMain().getElements());
    }

    public void set(int slot, ItemStack itemStack) {
        if(slot >= CategoryManager.getMain().getElements().size()) {
            while (slot >= CategoryManager.getMain().getElements().size()) {
                for (int j = 0; j < 49; j++) {
                    CategoryManager.getMain().getElements().add(Element.of(ItemStack.of(ItemTypes.GLASS_PANE)));
                }
            }
        }

        CategoryManager.getMain().getElements().set(slot, Element.of(itemStack));
        page.define(CategoryManager.getMain().getElements());
    }

    public Page getPage() {
        return page;
    }
}

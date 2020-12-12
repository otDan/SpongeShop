package ot.dan.SpongeShop.Guis;

import inventory.Element;
import inventory.Layout;
import inventory.Page;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import ot.dan.SpongeShop.Objects.Category;
import ot.dan.SpongeShop.Objects.CategoryManager;

public class ShopGUI {
    private final PluginContainer pluginContainer;
    private Page page;
    private final Category category;

    public ShopGUI(PluginContainer pluginContainer, Category category) {
        this.pluginContainer = pluginContainer;
        this.category = category;

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

        page.define(category.getElements());
    }

    public void set(int slot, ItemStack itemStack) {
        if(slot >= CategoryManager.getMain().getElements().size()) {
            while (slot >= CategoryManager.getMain().getElements().size()) {
                for (int j = 0; j < 49; j++) {
                    CategoryManager.getMain().getElements().add(Element.of(ItemStack.of(ItemTypes.GLASS_PANE)));
                }
            }
        }

        category.getElements().set(slot, Element.of(itemStack));
        page.define(category.getElements());
    }

    public Page getPage() {
        return page;
    }
}

package ot.dan.SpongeShop.Commands;

import com.erigitic.main.TotalEconomy;
import inventory.Action;
import inventory.Element;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.Actions;
import ot.dan.SpongeShop.Guis.CategoryGUI;
import ot.dan.SpongeShop.Objects.Category;
import ot.dan.SpongeShop.Objects.CategoryManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ShopCategorySetCMD implements CommandExecutor {
//    private final CategoryGUI categoryGUI;

    public ShopCategorySetCMD() {
//        this.categoryGUI = categoryGUI;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            Optional<ItemStack> item = player.getItemInHand(HandTypes.MAIN_HAND);
            if(item.isPresent()) {
                if(args.getOne("category").isPresent()) {
                    if (args.getOne("slot").isPresent()) {
                        Category category = CategoryManager.getCategory((String) args.getOne("category").get());
                        if(category != null) {

                            int slot = (Integer) args.getOne("slot").get();

                            List<Text> lore = new ArrayList<>();

                            if (args.getOne("buyprice").isPresent()) {
                                int buy = (int) args.getOne("buyprice").get();
                                if(buy != -1) {
                                    lore.add(Text.of("§bBuy: §f$" + buy));
                                }
                            }
                            if (args.getOne("sellprice").isPresent()) {
                                int sell = (int) args.getOne("sellprice").get();
                                if(sell != -1) {
                                    lore.add(Text.of("§bSell: §f$" + sell));
                                }
                            }

                            if(slot >= category.getElements().size()) {
                                while (slot >= category.getElements().size()) {
                                    for (int j = 0; j < 49; j++) {
                                        category.getElements().add(Element.of(ItemStack.of(ItemTypes.GLASS_PANE)));
                                    }
                                }
                            }

                            if (lore.size() > 0) {
                                item.get().offer(Keys.ITEM_LORE, lore);
                                Element element = Element.of(item.get(), Actions.getClickAction());
                                category.getElements().set(slot, element);
                                player.sendMessage(Text.of("§bSet item with action at slot " + slot));
                            } else {
                                Element element = Element.of(item.get());
                                category.getElements().set(slot, element);
                                player.sendMessage(Text.of("§bSet item with no action at slot " + slot));
                            }
                        }
                        else {
                            player.sendMessage(Text.of("§cCategory does not exist!"));
                        }
                    }
                }
                else {
                    player.sendMessage(Text.of("§cCategory does not exist!"));
                }
            }
        }
        return CommandResult.empty();
    }
}

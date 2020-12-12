package ot.dan.SpongeShop.Guis;

import com.erigitic.main.TotalEconomy;
import inventory.Action;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Actions {

    public static Consumer<Action.Click> clickAction = click -> {
        if(click.getEvent() instanceof ClickInventoryEvent.Primary) {
            Optional<UniqueAccount> account = TotalEconomy.getTotalEconomy().getAccountManager().getOrCreateAccount(click.getPlayer().getUniqueId());
            if(account.isPresent()) {
                BigDecimal bigDecimal = account.get().getBalance(TotalEconomy.getTotalEconomy().getDefaultCurrency());

                int price = 0;
                Optional<List<Text>> lore = click.getElement().getItem().get(Keys.ITEM_LORE);
                if (lore.isPresent()) {
                    for (Text text : lore.get()) {
                        if (text.toPlain().toLowerCase().contains("buy")) {
                            price = Integer.parseInt(text.toPlain().split("\\$")[1]);
                        }
                    }
                }

                if (bigDecimal.intValueExact() >= price) {
                    ItemStack itemStack = click.getElement().getItem().createStack();

                    InventoryTransactionResult transactionResult = click.getPlayer().getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class)).offer(itemStack);
                    if(transactionResult.getRejectedItems().size() == 0) {
                        BigDecimal result = account.get().getBalance(TotalEconomy.getTotalEconomy().getDefaultCurrency()).subtract(BigDecimal.valueOf(price));
                        Cause cause = Sponge.getCauseStackManager().getCurrentCause();
                        account.get().setBalance(TotalEconomy.getTotalEconomy().getDefaultCurrency(), result, cause);
                        click.getPlayer().sendMessage(Text.of("§bBought the item for §f$" + price));
                    }
                    else {
                        click.getPlayer().sendMessage(Text.of("§cNot enough space in your inventory"));
                    }
                }
                else {
                    click.getPlayer().sendMessage(Text.of("§cNot enough money to buy this item"));
                }


            }
        }
//        else if(click.getEvent() instanceof ClickInventoryEvent.Secondary) {
//            Optional<UniqueAccount> account = TotalEconomy.getTotalEconomy().getAccountManager().getOrCreateAccount(click.getPlayer().getUniqueId());
//            if(account.isPresent()) {
//                BigDecimal bigDecimal = account.get().getBalance(TotalEconomy.getTotalEconomy().getDefaultCurrency());
//            }
//        }
    };

    public static Consumer<Action.Click> getClickAction() {
        return clickAction;
    }

    public static void setClickAction(Consumer<Action.Click> clickAction) {
        Actions.clickAction = clickAction;
    }
}

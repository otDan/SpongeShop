package ot.dan.SpongeShop.Commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.CategoryGUI;
import ot.dan.SpongeShop.Objects.Category;
import ot.dan.SpongeShop.Objects.CategoryManager;

import java.util.Optional;

public class ShopSetCMD implements CommandExecutor {
    private final CategoryGUI categoryGUI;

    public ShopSetCMD(CategoryGUI categoryGUI) {
        this.categoryGUI = categoryGUI;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            Optional<ItemStack> item = player.getItemInHand(HandTypes.MAIN_HAND);
            if(item.isPresent()) {
                if(args.getOne("slot").isPresent()) {
                    if (args.getOne("category").isPresent()) {
                        Category category = CategoryManager.getCategory((String) args.getOne("category").get());
                        if (category != null) {
                            String categoryName = category.getName();
                            categoryGUI.setCategory((Integer) args.getOne("slot").get(), item.get(), categoryName);
                            player.sendMessage(Text.of("§bSet slot §f" + args.requireOne("slot") + " §bof chategory " + categoryName + " with item §f" + item.get().getType().getName()));
                        }
                        else if(((String) args.getOne("category").get()).equalsIgnoreCase("null")) {
                            categoryGUI.set((Integer) args.getOne("slot").get(), item.get());
                            player.sendMessage(Text.of("§bSet slot §f" + args.requireOne("slot") + " §bwith item §f" + item.get().getType().getName()));
                        }
                        else {
                            player.sendMessage(Text.of("§cCategory does not exist!"));
                        }
                    }
                }
            }
        }
        return CommandResult.empty();
    }
}

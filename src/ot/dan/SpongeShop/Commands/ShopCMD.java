package ot.dan.SpongeShop.Commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.CategoryGUI;

import java.util.Objects;

public class ShopCMD implements CommandExecutor {
    private final CategoryGUI categoryGUI;

    public ShopCMD(CategoryGUI categoryGUI) {
        this.categoryGUI = categoryGUI;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            categoryGUI.getPage().open(player, 0);
        }
        return CommandResult.empty();
    }
}

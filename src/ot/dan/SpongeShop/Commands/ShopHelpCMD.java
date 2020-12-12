package ot.dan.SpongeShop.Commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.CategoryGUI;

public class ShopHelpCMD implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            player.sendMessage(Text.of(""));
            player.sendMessage(Text.of("§bShop"));
            player.sendMessage(Text.of(""));
            player.sendMessage(Text.of("/shop set <slot> <category>"));
            player.sendMessage(Text.of("/shop <category> <slot> <buyprice> <sellprice>"));
            player.sendMessage(Text.of(""));
            Text text;

        }
        return CommandResult.empty();
    }
}

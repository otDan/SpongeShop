package ot.dan.SpongeShop.Commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ot.dan.SpongeShop.Guis.CategoryGUI;
import ot.dan.SpongeShop.Objects.Category;
import ot.dan.SpongeShop.Objects.CategoryManager;

public class CategoryCMD implements CommandExecutor {
    private int piece;

    public CategoryCMD(int piece) {
        this.piece = piece;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            switch (piece) {
                case 0:
                    player.sendMessage(Text.of(""));
                    player.sendMessage(Text.of("§bCategory"));
                    player.sendMessage(Text.of(""));
                    player.sendMessage(Text.of("/category [add,create] <category>"));
                    player.sendMessage(Text.of("/category [remove,delete] <category>"));
                    player.sendMessage(Text.of("/category list"));
                    player.sendMessage(Text.of(""));
                    break;
                case 1:
                    if(args.getOne("category").isPresent()) {
                        String name = (String) args.getOne("category").get();
                        Category category = CategoryManager.getCategory(name);
                        if(category == null) {
                            CategoryManager.getCategories().add(new Category(name));
                            player.sendMessage(Text.of("§bCreated the category §f" + name));
                        }
                        else {
                            player.sendMessage(Text.of("§cThe category §f" + name + " §calready exists!"));
                        }
                    }
                    break;
                case 2:
                    player.sendMessage(Text.of(""));
                    player.sendMessage(Text.of("§bCategories"));
                    player.sendMessage(Text.of(""));
                    int i = 0;
                    for (Category category:CategoryManager.getCategories()) {
                        player.sendMessage(Text.of(i + "- " + category.getName()));
                        i++;
                    }
                    player.sendMessage(Text.of(""));
                    break;
                case 3:
                    if(args.getOne("category").isPresent()) {
                        String name = (String) args.getOne("category").get();
                        Category category = CategoryManager.getCategory(name);
                        if(category == null) {
                            player.sendMessage(Text.of("§cThe category §f" + name + " §cdoes not exists!"));
                        }
                        else {
                            CategoryManager.getCategories().remove(category);
                            player.sendMessage(Text.of("§bRemoved the category §f" + name));
                        }
                    }
                    break;
                default:
                    player.sendMessage(Text.of(""));
                    player.sendMessage(Text.of("§bCategory"));
                    player.sendMessage(Text.of(""));
                    player.sendMessage(Text.of("/category [add,create] <category>"));
                    player.sendMessage(Text.of("/category [remove,delete] <category>"));
                    player.sendMessage(Text.of("/category list"));
                    player.sendMessage(Text.of(""));
                    break;
            }
        }
        return CommandResult.empty();
    }
}
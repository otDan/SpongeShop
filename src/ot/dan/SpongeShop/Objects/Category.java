package ot.dan.SpongeShop.Objects;

import inventory.Element;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Category {
    private String name;
    private List<Element> elements;

    public Category(String name) {
        this.name = name;
        this.elements = IntStream.rangeClosed(1, 49)
            .mapToObj(i -> Element.of(ItemStack.of(ItemTypes.GLASS_PANE)))
            .collect(Collectors.toList());
    }

    public Category(String name, List<Element> elements) {
        this.name = name;
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}

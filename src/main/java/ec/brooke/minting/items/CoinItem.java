package ec.brooke.minting.items;

import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CoinItem extends Item {
    public CoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        CoinDesign design = Objects.requireNonNullElse(
                stack.get(MintingComponents.COIN_DESIGN),
                CoinDesign.FALLBACK
        );

        return Component.translatableEscape(this.getDescriptionId(stack), Component.translatable(design.material().translationKey()));
    }
}

package ec.brooke.minting.items;

import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag
    ) {
        @Nullable String minter = stack.get(MintingComponents.MINTER);
        if (minter != null) tooltip.add(Component.translatableEscape("item.minting.coin.minter", minter).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}

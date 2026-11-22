package ec.brooke.minting.menus;

import ec.brooke.minting.blocks.MintingBlocks;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingDataMaps;
import ec.brooke.minting.items.MintingItems;
import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.DieShape;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoiningMenu extends ItemCombinerMenu {
    public static final int MATERIAL_SLOT = 0;
    public static final int DIE_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    public CoiningMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CoiningMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MintingMenus.COINING.get(), containerId, playerInventory, access);
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasStack) {
        return !this.resultSlots.isEmpty();
    }

    @Override
    protected void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        stack.shrink(1);
        slots.get(MATERIAL_SLOT).set(stack);
        stack.onCraftedBy(player.level(), player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, List.of(this.inputSlots.getItem(MATERIAL_SLOT), this.inputSlots.getItem(DIE_SLOT)));
        player.level().playSeededSound(null,
                player.getX(), player.getY(),
                player.getZ(), SoundEvents.ANVIL_LAND,
                player.getSoundSource(),
                1.0F, 2.0F,
                player.level().random.nextLong()
        );
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState state) {
        return state.is(MintingBlocks.COINING_PRESS);
    }

    @Override
    public void createResult() {
        ItemStack input = inputSlots.getItem(MATERIAL_SLOT);
        ItemStack die = inputSlots.getItem(DIE_SLOT);
        DieShape dieShape = getDieShape(die);

        if (input.isEmpty() || die.isEmpty() || dieShape == null) {
            resultSlots.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        ItemStack stack = MintingItems.COIN.toStack();
        ResourceLocation material = BuiltInRegistries.ITEM.getKey(input.getItem());
        stack.set(MintingComponents.COIN_DESIGN, new CoinDesign(dieShape.bitmap(), material));
        stack.setCount(input.getCount());
        resultSlots.setItem(OUTPUT_SLOT, stack);
    }

    @Override
    public int getSlotToQuickMoveTo(@NotNull ItemStack stack) {
        return getDieShape(stack) != null ? DIE_SLOT : MATERIAL_SLOT;
    }

    @Override
    protected boolean canMoveIntoInputSlots(@NotNull ItemStack stack) {
        return getDieShape(stack) != null || getMaterial(stack) != null;
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(MATERIAL_SLOT, 27, 47, stack -> getMaterial(stack) != null)
                .withSlot(DIE_SLOT, 76, 47, item -> getDieShape(item) != null)
                .withResultSlot(2, 134, 47)
                .build();
    }

    private @Nullable CoinMaterial getMaterial(ItemStack stack) {
        return stack.getItemHolder().getData(MintingDataMaps.COIN_MATERIALS);
    }

    private @Nullable DieShape getDieShape(ItemStack stack) {
        return stack.get(MintingComponents.DIE_SHAPE);
    }
}

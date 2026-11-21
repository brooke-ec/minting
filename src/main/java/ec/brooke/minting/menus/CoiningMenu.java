package ec.brooke.minting.menus;

import ec.brooke.minting.blocks.MintingBlocks;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingDataMaps;
import ec.brooke.minting.items.MintingItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        return false;
    }

    @Override
    protected void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        /*stack.onCraftedBy(player.level(), player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, List.of(this.inputSlots.getItem(MATERIAL_SLOT)));
        this.shrinkStackInSlot(MATERIAL_SLOT, stack.getCount());
        player.level().playSeededSound(null,
                player.getX(), player.getY(),
                player.getZ(), SoundEvents.ANVIL_USE,
                player.getSoundSource(),
                1.0F, 2.0F,
                player.level().random.nextLong()
        );*/
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState state) {
        return state.is(MintingBlocks.COINING_PRESS);
    }

    @Override
    public void createResult() {

    }

    @Override
    public int getSlotToQuickMoveTo(@NotNull ItemStack stack) {
        return stack.getItem() == MintingItems.STRIKING_DIE.get().asItem() ? DIE_SLOT : MATERIAL_SLOT;
    }

    @Override
    protected boolean canMoveIntoInputSlots(@NotNull ItemStack stack) {
        return stack.getItem() == MintingItems.STRIKING_DIE.get().asItem() || getMaterial(stack) != null;
    }
    
    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(MATERIAL_SLOT, 27, 47, stack -> getMaterial(stack) != null)
                .withSlot(DIE_SLOT, 76, 47, item -> item.getItem() == MintingItems.STRIKING_DIE.get().asItem())
                .withResultSlot(2, 134, 47)
                .build();
    }

    private void shrinkStackInSlot(int index, int amount) {
        ItemStack itemstack = this.inputSlots.getItem(index);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(amount);
            this.inputSlots.setItem(index, itemstack);
        }
    }

    private @Nullable CoinMaterial getMaterial(ItemStack stack) {
        return stack.getItemHolder().getData(MintingDataMaps.COIN_MATERIALS);
    }
}

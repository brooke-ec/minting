package ec.brooke.minting.menus;

import ec.brooke.minting.blocks.MintingBlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EngravingMenu extends ItemCombinerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private final Level level;

    public EngravingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public EngravingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MintingMenus.ENGRAVING_MENU.get(), containerId, playerInventory, access);
        this.level = playerInventory.player.level();
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasStack) {
        return false;
    }

    @Override
    protected void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        this.shrinkStackInSlot(INPUT_SLOT);
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState state) {
        return state.is(MintingBlocks.ENGRAVING_TABLE);
    }

    @Override
    public void createResult() {
        
    }

    @Override
    public int getSlotToQuickMoveTo(@NotNull ItemStack stack) {
        return INPUT_SLOT;
    }


    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(INPUT_SLOT, 151, 8, item -> true)
                .withResultSlot(OUTPUT_SLOT, 151, 52)
                .build();
    }

    private void shrinkStackInSlot(int index) {
        ItemStack itemstack = this.inputSlots.getItem(index);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
            this.inputSlots.setItem(index, itemstack);
        }
    }
}

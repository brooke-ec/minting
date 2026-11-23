package ec.brooke.minting.menus;

import ec.brooke.minting.blocks.MintingBlocks;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.items.MintingItems;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EngravingMenu extends ItemCombinerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final List<Item> INPUT_ITEMS = List.of(Items.NETHERITE_INGOT, MintingItems.STRIKING_DIE.get());
    @Nullable
    private Bitmap bitmap = null;

    public EngravingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public EngravingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MintingMenus.ENGRAVING.get(), containerId, playerInventory, access);
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasStack) {
        return isValidInput(inputSlots.getItem(INPUT_SLOT)) && bitmap != null;
    }

    @Override
    protected void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, List.of(this.inputSlots.getItem(INPUT_SLOT)));

        // I think there might be a bug here, because `stack` is different things depending on if quick move is used or not
        if (inputSlots.getItem(INPUT_SLOT).getCount() == stack.getCount()) this.inputSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
        else inputSlots.getItem(INPUT_SLOT).setCount(stack.getCount()); // quick move path (hopefully)

        player.level().playSeededSound(null,
                player.getX(), player.getY(),
                player.getZ(), SoundEvents.ANVIL_USE,
                player.getSoundSource(),
                1.0F, 2.0F,
                player.level().random.nextLong()
        );
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState state) {
        return state.is(MintingBlocks.ENGRAVING_TABLE);
    }

    @Override
    public void createResult() {
        ItemStack input = this.inputSlots.getItem(INPUT_SLOT);
        if (isValidInput(input) && this.bitmap != null) {
            ItemStack result = new ItemStack(MintingItems.STRIKING_DIE.get());
            result.setCount(input.getCount());
            result.set(MintingComponents.DIE_SHAPE, this.bitmap);
            this.resultSlots.setItem(OUTPUT_SLOT, result);
        } else {
            this.resultSlots.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
        }
    }

    @Override
    public int getSlotToQuickMoveTo(@NotNull ItemStack stack) {
        return INPUT_SLOT;
    }

    @Override
    protected boolean canMoveIntoInputSlots(@NotNull ItemStack stack) {
        return isValidInput(stack);
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(INPUT_SLOT, 151, 8, this::isValidInput)
                .withResultSlot(OUTPUT_SLOT, 151, 52)
                .build();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.createResult();
    }

    public boolean isValidInput(ItemStack stack) {
        return INPUT_ITEMS.contains(stack.getItem());
    }
}

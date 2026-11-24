package ec.brooke.minting.client.screens;

import ec.brooke.minting.Minting;
import ec.brooke.minting.menus.CoiningMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class CoiningScreen extends ItemCombinerScreen<CoiningMenu> {
    private static final ResourceLocation EMPTY_MATERIAL = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "item/empty_slot_nugget");
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/coining.png");
    private static final ResourceLocation EMPTY_DIE = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "item/empty_slot_die");

    public CoiningScreen(CoiningMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, BACKGROUND);
        this.titleLabelY = 8;
    }

    @Override
    protected void subInit() {
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics guiGraphics, int x, int y) {
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderIcon(menu.getSlot(CoiningMenu.MATERIAL_SLOT), EMPTY_MATERIAL, 0.5f, guiGraphics, leftPos, topPos);
        renderIcon(menu.getSlot(CoiningMenu.DIE_SLOT), EMPTY_DIE, 0.5f, guiGraphics, leftPos, topPos);
    }

    private void renderIcon(Slot slot, ResourceLocation icon, float alpha, GuiGraphics guiGraphics, int x, int y) {
        if (slot.hasItem()) return;
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(icon);
        guiGraphics.blit(x + slot.x, y + slot.y, 0, 16, 16, textureatlassprite, 1.0F, 1.0F, 1.0F, alpha);
    }
}

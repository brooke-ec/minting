package ec.brooke.minting.client.screens;

import ec.brooke.minting.Minting;
import ec.brooke.minting.menus.EngravingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EngravingScreen extends ItemCombinerScreen<EngravingMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/engraving.png");


    public EngravingScreen(EngravingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, BACKGROUND);
        this.titleLabelY = 8;
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics guiGraphics, int x, int y) {

    }
}

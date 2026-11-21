package ec.brooke.minting.client.screens;

import ec.brooke.minting.Minting;
import ec.brooke.minting.client.screens.widgets.EngravingPixel;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.menus.EngravingMenu;
import ec.brooke.minting.networking.ServerboundEngravePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EngravingScreen extends ItemCombinerScreen<EngravingMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/engraving.png");
    private static final ResourceLocation EMPTY_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");
    private static final List<ResourceLocation> INPUT_ICONS = List.of(EMPTY_INGOT);
    private static final Bitmap bitmap = new Bitmap(); // static so that the bitmap persists across screen openings
    private final CyclingSlotBackground inputBackground = new CyclingSlotBackground(EngravingMenu.INPUT_SLOT);

    public EngravingScreen(EngravingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, BACKGROUND);
        this.titleLabelY = 8;
    }

    @Override
    protected void subInit() {
        PacketDistributor.sendToServer(new ServerboundEngravePacket(bitmap));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                this.addRenderableWidget(new EngravingPixel(this, x, y));
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.inputBackground.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
    }

    @Override
    protected void containerTick() {
        inputBackground.tick(INPUT_ICONS);
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics guiGraphics, int x, int y) {
    }


    public Bitmap getBitmap() {
        return bitmap;
    }
}

package ec.brooke.minting.client.screens.widgets;

import ec.brooke.minting.Minting;
import ec.brooke.minting.client.screens.EngravingScreen;
import ec.brooke.minting.networking.ServerboundEngravePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class EngravingPixel extends AbstractWidget {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/die.png");
    private static final int GRID_X = 81;
    private static final int GRID_Y = 8;
    private static final int SIZE = 6;

    private final EngravingScreen screen;
    private final int x;
    private final int y;

    public EngravingPixel(EngravingScreen screen, int x, int y) {
        super(GRID_X + screen.getGuiLeft() + x * SIZE, GRID_Y + screen.getGuiTop() + y * SIZE, SIZE, SIZE, Component.empty());
        this.screen = screen;
        this.x = x;
        this.y = y;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int value = screen.getBitmap().get(this.x, this.y);

        if (value < 3) {
            float darkness = (3 - value) / (float) 3;
            guiGraphics.setColor(darkness, darkness, darkness, 1.0F);
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), SIZE, SIZE, x, y, 1, 1, 16, 16);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (this.isHovered()) guiGraphics.renderOutline(this.getX(), this.getY(), SIZE, SIZE, 0xFFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    @Override
    public void playDownSound(@NotNull SoundManager handler) {
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        SoundManager handler = Minecraft.getInstance().getSoundManager();
        int value = screen.getBitmap().get(x, y);

        if (button == 0) {
            if (value > 2) return;
            handler.play(SimpleSoundInstance.forUI(SoundEvents.METAL_BREAK, 2.0F));
            value++;
        } else {
            if (value < 1) return;
            handler.play(SimpleSoundInstance.forUI(SoundEvents.METAL_PLACE, 1.5F));
            value--;
        }

        screen.getBitmap().set(x, y, value);
        PacketDistributor.sendToServer(new ServerboundEngravePacket(screen.getBitmap()));
    }
}

package ec.brooke.minting.client.screens.widgets;

import ec.brooke.minting.Minting;
import ec.brooke.minting.client.screens.EngravingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class EngravingPixel extends AbstractWidget {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/die.png");
    private static final int GRID_X = 81;
    private static final int GRID_Y = 8;
    private static final int SIZE = 6;

    private final EngravingScreen screen;
    private final int x;
    private final int y;
    public boolean clickable = false;

    public EngravingPixel(EngravingScreen screen, int x, int y) {
        super(GRID_X + screen.getGuiLeft() + x * SIZE, GRID_Y + screen.getGuiTop() + y * SIZE, SIZE, SIZE, Component.empty());
        this.screen = screen;
        this.x = x;
        this.y = y;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int value = screen.getBitmap().get(this.x, this.y);

        if (value > 0) {
            float darkness = value / (float) 3;
            guiGraphics.setColor(darkness, darkness, darkness, 1.0F);
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), SIZE, SIZE, x, y, 1, 1, 16, 16);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (isHovered) guiGraphics.renderOutline(this.getX(), this.getY(), SIZE, SIZE, 0xFFFFFFFF);

        if (clickable && isHovered) {
            clickable = false;
            clickPixel();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    private void clickPixel() {
        SoundManager sound = Minecraft.getInstance().getSoundManager();
        int value = screen.getBitmap().get(x, y);

        if (screen.mouseButton == 0) {
            if (value < 1) return;
            sound.play(SimpleSoundInstance.forUI(SoundEvents.METAL_BREAK, 2.0F));
            value--;
        } else if (screen.mouseButton == 1) {
            if (value > 2) return;
            sound.play(SimpleSoundInstance.forUI(SoundEvents.METAL_PLACE, 1.5F));
            value++;
        }

        screen.getBitmap().set(x, y, value);
    }
}

package ec.brooke.minting.client.screens.widgets;

import ec.brooke.minting.Minting;
import ec.brooke.minting.client.screens.EngravingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EngravingActionButton extends AbstractWidget {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Minting.MODID, "textures/gui/engraving_action_buttons.png");
    private final EngravingScreen screen;
    private final ActionType action;

    public EngravingActionButton(EngravingScreen screen, ActionType action, int x, int y) {
        super(screen.getGuiLeft() + x, screen.getGuiTop() + y, action.getWidth(), action.getHeight(), action.getMessage());
        this.screen = screen;
        this.action = action;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int u = isHovered() ? 32 : 0;
        int v = action.getTextureY();
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), u, v, this.width, this.height, 64, 64);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        switch (action) {
            case CLEAR -> screen.getBitmap().clear();
            case LOAD -> screen.loadBitmapFromDie();
            case FILL -> screen.getBitmap().fill();
        }
    }

    public enum ActionType {
        LOAD,
        CLEAR,
        FILL;

        private int getWidth() {
            return switch (this) {
                case CLEAR -> 27;
                case LOAD -> 25;
                case FILL -> 22;
            };
        }

        private int getHeight() {
            return 10;
        }

        private int getTextureY() {
            return switch (this) {
                case CLEAR -> 10;
                case LOAD -> 20;
                case FILL -> 0;
            };
        }

        private Component getMessage() {
            return switch (this) {
                case CLEAR -> Component.literal("Clear");
                case FILL -> Component.literal("Fill");
                case LOAD -> Component.literal("Load");
            };
        }
    }
}

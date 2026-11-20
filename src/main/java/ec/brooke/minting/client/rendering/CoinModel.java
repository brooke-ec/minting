package ec.brooke.minting.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CoinModel extends BakedModelWrapper<BakedModel> {
    private final List<RenderType> types;

    private CoinModel(BakedModel baked, RenderType type) {
        super(baked);
        this.types = List.of(type);
    }

    public static BakedModel build(ResourceLocation id, NativeImage image, BakedModel template) {
        SpriteContents contents = new SpriteContents(id, new FrameSize(image.getWidth(), image.getHeight()), image, ResourceMetadata.EMPTY);
        TextureAtlasSprite sprite = new StandaloneSprite(id, contents);

        List<BlockElement> elements = UnbakedGeometryHelper.createUnbakedItemElements(0, sprite);
        IModelBuilder<?> builder = IModelBuilder.of(false, false, false, template.getTransforms(), ItemOverrides.EMPTY, sprite, RenderTypeGroup.EMPTY);

        UnbakedGeometryHelper.bakeElements(builder, elements, material -> sprite, BlockModelRotation.X0_Y0);
        BakedModel baked = builder.build();

        Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(image));
        return new CoinModel(baked, RenderType.entityCutout(id));
    }

    @Override
    public @NotNull List<BakedModel> getRenderPasses(@NotNull ItemStack stack, boolean fabulous) {
        return List.of(this);
    }

    @Override
    public @NotNull List<RenderType> getRenderTypes(@NotNull ItemStack stack, boolean fabulous) {
        return types;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext ctx, @NotNull PoseStack poseStack, boolean leftHand) {
        getTransforms().getTransform(ctx).apply(leftHand, poseStack);
        return this;
    }

    private static final class StandaloneSprite extends TextureAtlasSprite {
        StandaloneSprite(ResourceLocation id, SpriteContents contents) {
            super(id, contents, contents.width(), contents.height(), 0, 0);
        }

        @Override
        public float uvShrinkRatio() {
            return 0f;
        }
    }
}

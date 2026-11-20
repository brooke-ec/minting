package ec.brooke.minting.client.rendering;

import ec.brooke.minting.Minting;
import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(value = Dist.CLIENT, modid = Minting.MODID)
public class CoinModelOverride extends BakedModelWrapper<BakedModel> {
    private final ItemOverrides overrides = new ItemOverrides() {
        @Nullable
        @Override
        public BakedModel resolve(@NotNull BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            @Nullable CoinDesign design = stack.get(MintingComponents.COIN_DESIGN);
            if (design == null) return model;
            return CoinModelCache.get(design);
        }
    };

    public CoinModelOverride(BakedModel originalModel) {
        super(originalModel);
    }

    @SubscribeEvent
    static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().computeIfPresent(
                new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Minting.MODID, "coin"), "inventory"),
                (location, model) -> {
                    CoinModelCache.setTemplate(model);
                    return new CoinModelOverride(model);
                }
        );
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return overrides;
    }
}

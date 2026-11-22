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
public class CoinOverride extends BakedModelWrapper<BakedModel> {
    private static boolean enabled = true;

    private final ItemOverrides overrides = new ItemOverrides() {
        @Nullable
        @Override
        public BakedModel resolve(@NotNull BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            if (!enabled) return model;

            @Nullable CoinDesign design = stack.get(MintingComponents.COIN_DESIGN);
            if (design == null) design = CoinDesign.FALLBACK;
            return CoinCache.getModel(design);
        }
    };

    public CoinOverride(BakedModel originalModel) {
        super(originalModel);
    }

    @SubscribeEvent
    static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().computeIfPresent(
                new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Minting.MODID, "coin"), "inventory"),
                (location, model) -> {
                    CoinCache.setTemplate(model);
                    return new CoinOverride(model);
                }
        );
    }

    public static void setEnabled(boolean enabled) {
        
        CoinOverride.enabled = enabled;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return overrides;
    }
}

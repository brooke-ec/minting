package ec.brooke.minting.menus;

import ec.brooke.minting.Minting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MintingMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Minting.MODID);

    public static final Supplier<MenuType<EngravingMenu>> ENGRAVING = MENUS.register("engraving",
            () -> new MenuType<>(EngravingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<CoiningMenu>> COINING = MENUS.register("coining",
            () -> new MenuType<>(CoiningMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
}

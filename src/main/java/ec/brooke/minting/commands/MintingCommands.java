package ec.brooke.minting.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import ec.brooke.minting.Minting;
import ec.brooke.minting.client.rendering.CoinCache;
import ec.brooke.minting.client.rendering.CoinOverride;
import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.DieShape;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class MintingCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(Minting.MODID)
                        .then(Commands.literal("rendering")
                                .then(Commands.literal("enabled").then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    CoinOverride.setEnabled(enabled);
                                    CoinCache.reset();
                                    return Command.SINGLE_SUCCESS;
                                }))).then(Commands.literal("clean").executes(context -> {
                                    CoinCache.reset();
                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("modify")
                                .requires(context -> context.hasPermission(2))
                                .then(Commands.literal("invert").executes(context -> {
                                    ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();

                                    DieShape die = stack.get(MintingComponents.DIE_SHAPE);
                                    if (die != null) die.bitmap().invert();

                                    CoinDesign coin = stack.get(MintingComponents.COIN_DESIGN);
                                    if (coin != null) coin.bitmap().invert();

                                    return Command.SINGLE_SUCCESS;
                                })).then(Commands.literal("material")
                                        .then(Commands.argument("material", ItemArgument.item(event.getBuildContext())).executes(context -> {
                                            ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();
                                            CoinDesign design = stack.get(MintingComponents.COIN_DESIGN);
                                            ResourceLocation material = BuiltInRegistries.ITEM.getKey(ItemArgument.getItem(context, "material").getItem());
                                            if (design != null) stack.set(MintingComponents.COIN_DESIGN, new CoinDesign(design.bitmap(), material));
                                            else throw new IllegalArgumentException("Selected item is not a coin.");
                                            return Command.SINGLE_SUCCESS;
                                        }))))
        );
    }
}

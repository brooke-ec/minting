package ec.brooke.minting.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import ec.brooke.minting.Minting;
import ec.brooke.minting.client.rendering.CoinCache;
import ec.brooke.minting.client.rendering.CoinOverride;
import ec.brooke.minting.client.rendering.CoinTexture;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingRegistries;
import ec.brooke.minting.items.MintingItems;
import ec.brooke.minting.items.components.CoinDesign;
import ec.brooke.minting.items.components.MintingComponents;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@EventBusSubscriber
public class MintingCommands {
    private static final SimpleCommandExceptionType ERROR_NOT_COIN = new SimpleCommandExceptionType(Component.translatable("command.minting.not_coin"));

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(Minting.MODID)
                        .then(Commands.literal("render")
                                .then(Commands.literal("enable").then(Commands.argument("enabled", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                            CoinOverride.setEnabled(enabled);
                                            CoinCache.reset();

                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable(enabled ? "command.minting.render.enable.enabled" :
                                                                                 "command.minting.render.enable.disabled"),
                                                    false
                                            );

                                            return Command.SINGLE_SUCCESS;
                                        })))
                                .then(Commands.literal("rebuild").executes(context -> {
                                    CoinCache.reset();

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("command.minting.render.rebuild.success"),
                                            false
                                    );

                                    return Command.SINGLE_SUCCESS;
                                })).then(Commands.literal("save").executes(context -> {
                                    ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();
                                    @Nullable CoinDesign coin = stack.get(MintingComponents.COIN_DESIGN);
                                    if (coin == null) throw ERROR_NOT_COIN.create();

                                    File directory = new File(Minecraft.getInstance().gameDirectory, Screenshot.SCREENSHOT_DIR);
                                    File target = getFile(directory);

                                    try (NativeImage image = CoinTexture.build(coin)) {
                                        Files.createDirectories(directory.toPath());
                                        image.writeToFile(target);
                                    } catch (IOException e) {
                                        Minting.LOGGER.error("Failed to save coin render to file", e);
                                        throw SaveException.create(e.toString());
                                    }

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("command.minting.render.save.success",
                                                    Component.literal(target.getName()).withStyle(s -> s.withUnderlined(true)
                                                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, target.getAbsolutePath())))
                                            ),
                                            false
                                    );

                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("modify")
                                .requires(context -> context.hasPermission(2))
                                .then(Commands.literal("invert")
                                        .executes(context -> {
                                            ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();

                                            @Nullable CoinDesign coin = stack.get(MintingComponents.COIN_DESIGN);
                                            if (coin != null) stack.set(MintingComponents.COIN_DESIGN,
                                                    new CoinDesign(coin.materialKey(), coin.bitmap().inverted())
                                            );

                                            @Nullable Bitmap die = stack.get(MintingComponents.DIE_SHAPE);
                                            if (die != null) stack.set(MintingComponents.DIE_SHAPE, die.inverted());

                                            if (die == null && coin == null) throw ERROR_NOT_COIN.create();

                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable("command.minting.modify.invert.success", stack.getDisplayName()),
                                                    false
                                            );

                                            return Command.SINGLE_SUCCESS;
                                        })).then(Commands.literal("material")
                                        .then(Commands.argument("material", ResourceArgument.resource(
                                                event.getBuildContext(),
                                                MintingRegistries.COIN_MATERIAL
                                        )).executes(context -> {
                                            ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();
                                            @Nullable CoinDesign design = stack.get(MintingComponents.COIN_DESIGN);
                                            ResourceKey<CoinMaterial> key = ResourceArgument.getResource(context, "material",
                                                    MintingRegistries.COIN_MATERIAL
                                            ).key();

                                            if (design != null) stack.set(MintingComponents.COIN_DESIGN, new CoinDesign(key, design.bitmap()));
                                            else throw ERROR_NOT_COIN.create();

                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable("command.minting.modify.material.success", key.location().toString()),
                                                    false
                                            );

                                            return Command.SINGLE_SUCCESS;
                                        }))).then(Commands.literal("minter")
                                        .then(Commands.argument("name", StringArgumentType.string())
                                                .executes(context -> {
                                                    ItemStack stack = context.getSource().getPlayerOrException().getInventory().getSelected();
                                                    if (stack.getItem() != MintingItems.COIN.asItem()) throw ERROR_NOT_COIN.create();
                                                    stack.set(MintingComponents.MINTER, StringArgumentType.getString(context, "name"));

                                                    context.getSource().sendSuccess(
                                                            () -> Component.translatable("command.minting.modify.minter.success",
                                                                    StringArgumentType.getString(context, "name")
                                                            ),
                                                            false
                                                    );

                                                    return Command.SINGLE_SUCCESS;
                                                }))).then(Commands.literal("dieify")
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();

                                            ItemStack stack = player.getInventory().getSelected();
                                            @Nullable CoinDesign design = stack.get(MintingComponents.COIN_DESIGN);
                                            if (design == null) throw ERROR_NOT_COIN.create();

                                            ItemStack die = MintingItems.STRIKING_DIE.toStack();
                                            die.set(MintingComponents.DIE_SHAPE, design.bitmap());
                                            player.getInventory().setItem(player.getInventory().selected, die);

                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable("command.minting.modify.dieify.success"),
                                                    false
                                            );

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
        );
    }

    private static File getFile(File gameDirectory) {
        String s = Util.getFilenameFormattedDateTime();
        int i = 1;

        while (true) {
            File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");
            if (!file1.exists()) {
                return file1;
            }

            i++;
        }
    }
}

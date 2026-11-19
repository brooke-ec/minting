package ec.brooke.minting.blocks;

import com.mojang.serialization.MapCodec;
import ec.brooke.minting.menus.EngravingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static ec.brooke.minting.blocks.MintingBlocks.BLOCK_CODECS;

public class EngravingTableBlock extends Block {
    public static final Supplier<MapCodec<EngravingTableBlock>> CODEC = BLOCK_CODECS.register("engraving_table",
            () -> BlockBehaviour.simpleCodec(EngravingTableBlock::new)
    );

    public EngravingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC.get();
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player,
            @NotNull BlockHitResult hitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider(
                (id, inventory, player) -> new EngravingMenu(id, inventory,
                        ContainerLevelAccess.create(level, pos)
                ), Component.translatable("menu.title.minting.engraving")
        );
    }
}

package pepjebs.choruslinks.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;

public class ChorusLinkBlock extends BlockWithEntity {

    public ChorusLinkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        var blockstate = super.onBreak(world, pos, state, player);
        GlobalPos queryPos = GlobalPos.create(world.getRegistryKey(), pos);
        world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).getChorusLinkPositions().remove(queryPos);
        return blockstate;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChorusLinkBlockEntity(ChorusLinksMod.CHORUS_LINK_ENTITY_TYPE, pos, state);
    }

    @Nullable
    public MapCodec<ChorusLinkBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ChorusLinksMod.CHORUS_LINK_ENTITY_TYPE, ChorusLinkBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}

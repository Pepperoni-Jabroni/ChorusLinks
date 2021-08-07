package pepjebs.choruslinks.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.block.entity.ChorusLinkBlockEntity;

public class ChorusLinkBlock extends BlockWithEntity implements BlockEntityProvider {

    public ChorusLinkBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChorusLinkBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type,
                ChorusLinksMod.CHORUS_LINK_BLOCK_ENTITY_TYPE,
                (world1, pos, state1, be) -> ChorusLinkBlockEntity.tick(world1, pos, state1, be));
    }
}

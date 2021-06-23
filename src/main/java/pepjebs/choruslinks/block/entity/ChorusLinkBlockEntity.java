package pepjebs.choruslinks.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.block.ChorusLinkBlock;

public class ChorusLinkBlockEntity extends BlockEntity {
    public ChorusLinkBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusLinksMod.CHORUS_LINK_BLOCK_ENTITY_TYPE, pos, state);
    }
}

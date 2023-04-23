package pepjebs.choruslinks.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import pepjebs.choruslinks.ChorusLinksMod;

public class ChorusLinkBlockEntity extends BlockEntity {

    public ChorusLinkBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusLinksMod.CHORUS_LINK_ENTITY_TYPE, pos, state);
    }

    public ChorusLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChorusLinkBlockEntity blockEntity) {
        GlobalPos queryPos = GlobalPos.create(world.getRegistryKey(), pos);
        if (!world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).getChorusLinkPositions().contains(queryPos)) {
            world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).addToPositions(world, pos);
        }
    }
}

package pepjebs.choruslinks.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pepjebs.choruslinks.ChorusLinksMod;

import java.util.ArrayList;
import java.util.List;

public class ChorusLinkBlockEntity extends BlockEntity {

    public static final List<Pair<World, BlockPos>> chorusLinkPositions = new ArrayList<>();

    public ChorusLinkBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusLinksMod.CHORUS_LINK_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChorusLinkBlockEntity be) {
        Pair<World, BlockPos> queryPos = new Pair<>(world, pos);
        if (!chorusLinkPositions.contains(queryPos)) {
            chorusLinkPositions.add(queryPos);
        }
    }
}

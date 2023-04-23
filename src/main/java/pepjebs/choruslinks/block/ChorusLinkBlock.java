package pepjebs.choruslinks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;

public class ChorusLinkBlock extends Block {

    public ChorusLinkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        GlobalPos queryPos = GlobalPos.create(world.getRegistryKey(), pos);
        if (!world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).getChorusLinkPositions().contains(queryPos)) {
            world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).addToPositions(world, pos);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        GlobalPos queryPos = GlobalPos.create(world.getRegistryKey(), pos);
        world.getComponent(ChorusLinksMod.LINK_LOCATIONS_KEY).getChorusLinkPositions().remove(queryPos);
    }
}

package pepjebs.choruslinks.utils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

public class ChorusLinksUtils {

    public static int CHORUS_LINK_RADIUS = 64;

    public static BlockPos doChorusLinkSearch(ItemStack stack, World world, LivingEntity user) {
        BlockPos nearestChorusLink = null;
        double nearestSoFar = Double.MAX_VALUE;
        int radius = CHORUS_LINK_RADIUS;
        if (stack.getItem() instanceof GoldenChorusFruitItem) {
            radius *= ((GoldenChorusFruitItem) stack.getItem()).getRadiusMultiplier();
        }
        for (int i = -1 * radius; i < radius; i++) {
            for (int j = -1 * radius; j < radius; j++) {
                for (int k = -1 * radius; k < radius; k++) {
                    Vec3d targetVec = new Vec3d(user.getX() + i, user.getY() + j, user.getZ() + k);
                    double playerDist = user.getPos().distanceTo(targetVec);
                    if (playerDist <= radius) {
                        BlockPos blockPos = new BlockPos(targetVec);
                        if (world.isChunkLoaded(blockPos)) {
                            BlockState state = world.getBlockState(blockPos);
                            if (state.getBlock() instanceof ChorusLinkBlock && nearestSoFar > playerDist) {
                                nearestChorusLink = blockPos;
                                nearestSoFar = playerDist;
                            }
                        }
                    }
                }
            }
        }
        return nearestChorusLink;
    }
}

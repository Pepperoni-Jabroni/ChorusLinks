package pepjebs.choruslinks.utils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.block.entity.ChorusLinkBlockEntity;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

import java.util.List;
import java.util.stream.Collectors;

public class ChorusLinksUtils {

    public static int CHORUS_LINK_RADIUS = 64;

    public static BlockPos doChorusFruitConsume(ItemStack stack, World world, LivingEntity user) {
        if (stack.getItem() instanceof GoldenChorusFruitItem && stack.hasGlint()
                && stack.getOrCreateTag().contains(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG)) {
            int[] blockPos = stack.getOrCreateTag().getIntArray(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG);
            String boundDim = stack.getOrCreateTag().getString(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_DIM_TAG);
            if (blockPos.length == 3 && boundDim.compareTo(world.getRegistryKey().getValue().toString()) == 0)
                return new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
        }
        return doChorusLinkSearch(stack, world, user);
    }

    public static BlockPos doChorusLinkSearch(ItemStack stack, World world, LivingEntity user) {
        List<ChorusLinkBlockEntity> chorusLinks = world.blockEntities
                .stream()
                .filter(be -> be instanceof ChorusLinkBlockEntity)
                .map(be -> (ChorusLinkBlockEntity) be)
                .collect(Collectors.toList());
        BlockPos nearestChorusLink = null;
        double nearestSoFar = Double.MAX_VALUE;
        int radius = CHORUS_LINK_RADIUS;
        if (stack.getItem() instanceof GoldenChorusFruitItem) {
            radius *= ((GoldenChorusFruitItem) stack.getItem()).getRadiusMultiplier();
        }
        for (ChorusLinkBlockEntity link : chorusLinks) {
            BlockPos targetPos = link.getPos();
            if (targetPos.isWithinDistance(user.getPos(), radius) && world.isChunkLoaded(targetPos)) {
                BlockState state = world.getBlockState(targetPos);
                double playerDist = targetPos.getSquaredDistance(user.getPos(), true);
                if (state.getBlock() instanceof ChorusLinkBlock && nearestSoFar > playerDist) {
                    nearestChorusLink = targetPos;
                    nearestSoFar = playerDist;
                }
            }
        }
        return nearestChorusLink;
    }
}

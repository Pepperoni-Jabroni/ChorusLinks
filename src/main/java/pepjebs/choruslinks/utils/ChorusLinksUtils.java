package pepjebs.choruslinks.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
            int[] blockPosCoords = stack.getOrCreateTag().getIntArray(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG);
            String boundDim = stack.getOrCreateTag().getString(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_DIM_TAG);
            if (blockPosCoords.length == 3 && boundDim.compareTo(world.getRegistryKey().getValue().toString()) == 0) {
                BlockPos blockPos = new BlockPos(blockPosCoords[0], blockPosCoords[1], blockPosCoords[2]);
                if (world.isChunkLoaded(blockPos)
                        && world.getBlockState(blockPos).getBlock() instanceof ChorusLinkBlock) {
                    return blockPos;
                }
            }
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

    public static boolean doesBoundPosEqualBlockPos(ItemStack stack, BlockPos pos) {
        if (stack.getItem() instanceof GoldenChorusFruitItem && stack.hasGlint()
                && stack.getOrCreateTag().contains(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG)) {
            int[] blockPos = stack.getOrCreateTag().getIntArray(GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG);
            if (blockPos.length == 3) {
                return blockPos[0] == pos.getX() && blockPos[1] == pos.getY() && blockPos[2] == pos.getZ();
            }
        }
        return false;
    }

    public static void doChorusLinkTeleport(ItemStack usingStack, World world, LivingEntity user, BlockPos blockPos) {
        if (user.hasVehicle()) {
            user.stopRiding();
        }
        if (user.teleport(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, true)) {
            SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
            user.playSound(soundEvent, 1.0F, 1.0F);
        }
        if (user instanceof PlayerEntity) {
            ((PlayerEntity)user).getItemCooldownManager().set(usingStack.getItem(), 20);
        }
    }
}

package pepjebs.choruslinks.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GoldenChorusFruitItem extends Item {

    public static String GOLDEN_CHORUS_BIND_POS_TAG = "bound_pos";
    public static String GOLDEN_CHORUS_BIND_DIM_TAG = "bound_dim";

    public GoldenChorusFruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // user.eatFood always decrements ItemStack by 1, but Enchanted Golden Chorus Fruits
        // should be damaged instead
        if (world.isClient()) return stack;
        if (!(user instanceof ServerPlayerEntity)) return stack;
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
        Pair<BlockPos, ServerWorld> targetChorusLink =
                ChorusLinksUtils.doChorusFruitConsume(stack, world, serverPlayerEntity);
        if (targetChorusLink.getLeft() != null) {
            if (!ChorusLinksUtils.doesBoundPosEqualBlockPos(stack, targetChorusLink.getLeft())) {
                stack.remove(ChorusLinksMod.GOLDEN_CHORUS_BIND_POS_TAG);
                stack.remove(ChorusLinksMod.GOLDEN_CHORUS_BIND_DIM_TAG);
            }
            ChorusLinksUtils.doChorusLinkTeleport(stack, targetChorusLink.getRight(), serverPlayerEntity, targetChorusLink.getLeft());
        } else {
            stack.remove(ChorusLinksMod.GOLDEN_CHORUS_BIND_POS_TAG);
            stack.remove(ChorusLinksMod.GOLDEN_CHORUS_BIND_DIM_TAG);
            ChorusLinksUtils.doVanillaChorusFruitConsumption(stack, world, serverPlayerEntity);
        }
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
        user.emitGameEvent(GameEvent.EAT);
        ((ServerPlayerEntity) user).getHungerManager().eat(stack.get(DataComponentTypes.FOOD));
        if (!serverPlayerEntity.isCreative()) {
            if (stack.isDamageable()) {
                stack.damage(1, (PlayerEntity)user);
            } else {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        if (hasGlint(stack)) {
            if (stack.contains(ChorusLinksMod.GOLDEN_CHORUS_BIND_POS_TAG)) {
                String blockPosStr = stack.get(ChorusLinksMod.GOLDEN_CHORUS_BIND_POS_TAG);
                int[] blockPos = Arrays.stream(blockPosStr.split(",")).flatMapToInt(
                        s -> IntStream.of(Integer.parseInt(s))).toArray();
                if (blockPos.length == 3) {
                    textConsumer.accept(Text.translatable(
                            "item.chorus_links.tooltip.golden_chorus_fruit.bound_1",
                            blockPos[0],
                            blockPos[1],
                            blockPos[2]
                    ).formatted(Formatting.GRAY));
                }
                String boundDim = stack.get(ChorusLinksMod.GOLDEN_CHORUS_BIND_DIM_TAG);
                if (boundDim != null && !boundDim.isEmpty()) {
                    String[] parts = boundDim.split(":");
                    if (parts.length >= 2) {
                        String path = parts[1];
                        textConsumer.accept(Text.translatable(
                                "item.chorus_links.tooltip.golden_chorus_fruit.bound_2",
                                Arrays.stream(path.split("_"))
                                        .map(str -> str.substring(0, 1).toUpperCase() + str.substring(1))
                                        .collect(Collectors.joining(" "))
                        ).formatted(Formatting.GRAY));
                    }
                }
            } else {
                textConsumer.accept(
                    Text.translatable("item.chorus_links.tooltip.golden_chorus_fruit.unbound")
                        .formatted(Formatting.GRAY));
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.getItem().getTranslationKey().contains("enchanted");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (hasGlint(context.getStack())) {
            BlockPos pos = context.getBlockPos();
            BlockState state = context.getWorld().getBlockState(pos);
            if (state.getBlock() instanceof ChorusLinkBlock) {
                context.getStack().set(
                        ChorusLinksMod.GOLDEN_CHORUS_BIND_POS_TAG,
                        "%d,%d,%d".formatted(pos.getX(), pos.getY(), pos.getZ())
                );
                context.getStack().set(ChorusLinksMod.GOLDEN_CHORUS_BIND_DIM_TAG, context.getWorld().getRegistryKey().getValue().toString());
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }
}

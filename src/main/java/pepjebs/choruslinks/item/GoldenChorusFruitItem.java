package pepjebs.choruslinks.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GoldenChorusFruitItem extends Item {

    public static String GOLDEN_CHORUS_BIND_POS_TAG = "BoundPos";
    public static String GOLDEN_CHORUS_BIND_DIM_TAG = "BoundDim";

    private final int radiusMultiplier;

    public GoldenChorusFruitItem(Settings settings, int radiusMultiplier1) {
        super(settings);
        this.radiusMultiplier = radiusMultiplier1;
    }

    @Override
    public boolean isFood() {
        return true;
    }

    @Nullable
    @Override
    public FoodComponent getFoodComponent() {
        return (new FoodComponent.Builder()).hunger(4).saturationModifier(1.2F).alwaysEdible().build();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient) return super.finishUsing(stack, world, user);
        if (!(user instanceof ServerPlayerEntity)) return super.finishUsing(stack, world, user);
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusFruitConsume(stack, world, serverPlayerEntity);
        if (targetChorusLink != null) {
            if (!ChorusLinksUtils.doesBoundPosEqualBlockPos(stack, targetChorusLink)) {
                stack.removeSubTag(GOLDEN_CHORUS_BIND_POS_TAG);
                stack.removeSubTag(GOLDEN_CHORUS_BIND_DIM_TAG);
            }
            ChorusLinksUtils.doChorusLinkTeleport(stack, world, serverPlayerEntity, targetChorusLink);
        } else {
            stack.removeSubTag(GOLDEN_CHORUS_BIND_POS_TAG);
            stack.removeSubTag(GOLDEN_CHORUS_BIND_DIM_TAG);
            ChorusLinksUtils.doVanillaChorusFruitConsumption(stack, world, serverPlayerEntity);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (hasGlint(stack)) {
            if (stack.getOrCreateTag().contains(GOLDEN_CHORUS_BIND_POS_TAG)) {
                int[] blockPos = stack.getOrCreateTag().getIntArray(GOLDEN_CHORUS_BIND_POS_TAG);
                if (blockPos.length == 3) {
                    tooltip.add(new TranslatableText(
                            "item.chorus_links.tooltip.golden_chorus_fruit.bound_1",
                            blockPos[0],
                            blockPos[1],
                            blockPos[2]
                    ).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
                }
                String boundDim = stack.getOrCreateTag().getString(GOLDEN_CHORUS_BIND_DIM_TAG);
                if (boundDim != null && !boundDim.isEmpty()) {
                    String[] parts = boundDim.split(":");
                    if (parts.length >= 2) {
                        String path = parts[1];
                        tooltip.add(new TranslatableText(
                                "item.chorus_links.tooltip.golden_chorus_fruit.bound_2",
                                Arrays.stream(path.split("_"))
                                        .map(str -> str.substring(0, 1).toUpperCase() + str.substring(1))
                                        .collect(Collectors.joining(" "))
                        ).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
                    }
                }
            } else {
                tooltip.add(new TranslatableText("item.chorus_links.tooltip.golden_chorus_fruit.unbound")
                        .formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
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
                CompoundTag tag = context.getStack().getOrCreateTag();
                tag.putIntArray(GOLDEN_CHORUS_BIND_POS_TAG, Arrays.asList(pos.getX(), pos.getY(), pos.getZ()));
                tag.putString(GOLDEN_CHORUS_BIND_DIM_TAG, context.getWorld().getRegistryKey().getValue().toString());
                context.getStack().setTag(tag);
            }
        }
        return super.useOnBlock(context);
    }

    public int getRadiusMultiplier() { return radiusMultiplier; }
}

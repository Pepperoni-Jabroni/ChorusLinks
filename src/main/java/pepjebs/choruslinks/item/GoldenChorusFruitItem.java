package pepjebs.choruslinks.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

import java.util.Arrays;
import java.util.List;

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
        ItemStack is = super.finishUsing(stack, world, user);
        if (world.isClient) return is;
        ChorusLinksMod.LOGGER.info("Consumed Golden Chorus Fruit...");
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusFruitConsume(stack, world, user);
        if (!ChorusLinksUtils.doesBoundPosEqualBlockPos(stack, targetChorusLink)) {
            stack.removeSubTag(GOLDEN_CHORUS_BIND_POS_TAG);
            stack.removeSubTag(GOLDEN_CHORUS_BIND_DIM_TAG);
        }
        if (targetChorusLink != null) {
            user.teleport(targetChorusLink.getX() + 0.5,
                    targetChorusLink.getY() + 1,
                    targetChorusLink.getZ() + 0.5);
        } else {
            Items.CHORUS_FRUIT.finishUsing(stack, world, user);
        }
        return is;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (hasGlint(stack)) {
            if (stack.getOrCreateTag().contains(GOLDEN_CHORUS_BIND_POS_TAG)) {
                int[] blockPos = stack.getOrCreateTag().getIntArray(GOLDEN_CHORUS_BIND_POS_TAG);
                if (blockPos.length == 3) {
                    tooltip.add(new TranslatableText(
                            "item.chorus_links.tooltip.golden_chorus_fruit.bound",
                            blockPos[0],
                            blockPos[1],
                            blockPos[2]
                    ).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
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

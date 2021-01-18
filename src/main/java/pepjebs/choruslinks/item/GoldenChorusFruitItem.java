package pepjebs.choruslinks.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
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

    public static String GOLDEN_CHORUS_BINDING_TAG = "BoundTo";

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
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusLinkSearch(stack, world, user);
        if (targetChorusLink != null) {
            user.teleport(targetChorusLink.getX() + 0.5,
                    targetChorusLink.getY() + 1,
                    targetChorusLink.getZ() + 0.5);
        }
        return is;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (hasGlint(stack)) {
            if (stack.getOrCreateTag().contains(GOLDEN_CHORUS_BINDING_TAG)) {
                int[] blockPos = stack.getOrCreateTag().getIntArray(GOLDEN_CHORUS_BINDING_TAG);
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
                tag.putIntArray(GOLDEN_CHORUS_BINDING_TAG, Arrays.asList(pos.getX(), pos.getY(), pos.getZ()));
                context.getStack().setTag(tag);
            }
        }
        return super.useOnBlock(context);
    }

    public int getRadiusMultiplier() { return radiusMultiplier; }
}

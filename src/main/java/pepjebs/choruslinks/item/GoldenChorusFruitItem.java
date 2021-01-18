package pepjebs.choruslinks.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

public class GoldenChorusFruitItem extends Item {
    public GoldenChorusFruitItem(Settings settings) {
        super(settings);
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
}

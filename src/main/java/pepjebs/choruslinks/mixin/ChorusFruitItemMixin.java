package pepjebs.choruslinks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pepjebs.choruslinks.ChorusLinksMod;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {

    @Inject(method = "finishUsing", at = @At("INVOKE"), cancellable = true)
    private void onFinishUsingDoChorusLinkSearch(
            ItemStack stack,
            World world,
            LivingEntity user,
            CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) return;
        // Not sure why this is printing multiple times
        ChorusLinksMod.LOGGER.info("Consumed Chorus Fruit...");
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusLinkSearch(stack, world, user);
        if (targetChorusLink != null) {
            user.teleport(targetChorusLink.getX() + 0.5,
                    targetChorusLink.getY() + 1,
                    targetChorusLink.getZ() + 0.5);
            cir.setReturnValue(stack);
            cir.cancel();
        }
    }
}

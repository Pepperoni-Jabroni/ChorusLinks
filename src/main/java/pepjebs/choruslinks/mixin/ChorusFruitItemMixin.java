package pepjebs.choruslinks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pepjebs.choruslinks.ChorusLinksMod;

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
    }
}

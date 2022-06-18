package pepjebs.choruslinks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
        if (!(user instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusLinkSearch(stack, world, serverPlayerEntity);
        if (targetChorusLink != null) {
            ChorusLinksMod.LOGGER.info("Going to "+targetChorusLink.toShortString());
            ChorusLinksUtils.doChorusLinkTeleport(stack, (ServerWorld) world, serverPlayerEntity, targetChorusLink);
            // Basically "super.finishUsing"
            stack = user.eatFood(world, stack);
            ChorusLinksMod.LOGGER.info("Returning...");
            cir.setReturnValue(stack);
        } else {
            ChorusLinksMod.LOGGER.info("No valid target chorus_link found.");
        }
    }
}

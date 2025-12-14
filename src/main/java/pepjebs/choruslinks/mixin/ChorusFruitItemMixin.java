package pepjebs.choruslinks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pepjebs.choruslinks.utils.ChorusLinksUtils;

@Mixin(TeleportRandomlyConsumeEffect.class)
public class ChorusFruitItemMixin {

    @Inject(method = "onConsume", at = @At("INVOKE"), cancellable = true)
    private void onFinishUsingDoChorusLinkSearch(
            World world,
            ItemStack stack,
            LivingEntity user,
            CallbackInfoReturnable<Boolean> cir) {
        if (world.isClient()) return;
        if (!(user instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
        BlockPos targetChorusLink = ChorusLinksUtils.doChorusLinkSearch(stack, world, serverPlayerEntity);
        if (targetChorusLink != null) {
            ChorusLinksUtils.doChorusLinkTeleport(stack, (ServerWorld) world, serverPlayerEntity, targetChorusLink);
            cir.setReturnValue(true);
        }
    }
}

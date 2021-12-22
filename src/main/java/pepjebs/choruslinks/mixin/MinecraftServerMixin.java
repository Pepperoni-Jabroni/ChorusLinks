package pepjebs.choruslinks.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.choruslinks.utils.ChorusLinkLocationList;

import java.util.Map;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    private Map<RegistryKey<World>, ServerWorld> worlds;

    private final ChorusLinkLocationList operate = new ChorusLinkLocationList();

    public ChorusLinkLocationList getChorusLinkLocationList() {
        return this.operate;
    }

    private void initLogMeState(PersistentStateManager persistentStateManager) {
        persistentStateManager.getOrCreate(this.getChorusLinkLocationList()::readNbt, this.getChorusLinkLocationList()::createState, "logme");
    }

    @Inject(at = @At("RETURN"), method = "createWorlds")
    public void createWorlds(CallbackInfo info) {
        this.initLogMeState(this.worlds.get(World.OVERWORLD).getPersistentStateManager());
    }
}

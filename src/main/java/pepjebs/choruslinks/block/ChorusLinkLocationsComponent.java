package pepjebs.choruslinks.block;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import pepjebs.choruslinks.ChorusLinksMod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChorusLinkLocationsComponent implements AutoSyncedComponent, ComponentV3, WorldComponentInitializer {

    private static final Set<GlobalPos> chorusLinkPositions = new HashSet<>();

    @Override
    public void readFromNbt(NbtCompound tag) {
        int size = tag.getInt("size");
        for (int i = 0; i < size; i++) {
            String entry = tag.getString("elem_" + i);
            String[] entries = entry.split("::");
            String dim = entries[0];
            var coords = Arrays.stream(entries[1].split(",")).map(Integer::parseInt).toList();
            chorusLinkPositions.add(GlobalPos.create(
                    RegistryKey.of(RegistryKeys.WORLD, new Identifier(dim)),
                    new BlockPos(coords.get(0), coords.get(1), coords.get(2))));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("size", chorusLinkPositions.size());
        List<GlobalPos> posList = chorusLinkPositions.stream().toList();
        for (int i = 0; i < posList.size(); i++) {
            tag.putString("elem_" + i,
                    posList.get(i).getDimension().getValue().toString()
                            + "::"
                            + posList.get(i).getPos().getX()
                            + ","
                            + posList.get(i).getPos().getY()
                            + ","
                            + posList.get(i).getPos().getZ());
        }
    }

    public Set<GlobalPos> getChorusLinkPositions() {
        return chorusLinkPositions;
    }

    public void addToPositions(World world, BlockPos pos) {
        chorusLinkPositions.add(GlobalPos.create(world.getRegistryKey(), pos));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChorusLinkLocationsComponent comp)) return false;
        return comp.getChorusLinkPositions().equals(this.getChorusLinkPositions());
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(ChorusLinksMod.LINK_LOCATIONS_KEY, world -> new ChorusLinkLocationsComponent());
    }
}

package pepjebs.choruslinks.block;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import pepjebs.choruslinks.ChorusLinksMod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChorusLinkLocationsComponent implements AutoSyncedComponent, ComponentV3, WorldComponentInitializer {

    private static final Set<GlobalPos> chorusLinkPositions = new HashSet<>();

    @Override
    public void readData(ReadView readView) {
        int size = readView.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            String entry = readView.getString("elem_" + i, "");
            String[] entries = entry.split("::");
            String dim = entries[0];
            var coords = Arrays.stream(entries[1].split(",")).map(Integer::parseInt).toList();
            chorusLinkPositions.add(GlobalPos.create(
                    RegistryKey.of(RegistryKeys.WORLD, Identifier.of(dim)),
                    new BlockPos(coords.get(0), coords.get(1), coords.get(2))));
        }
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putInt("size", chorusLinkPositions.size());
        List<GlobalPos> posList = chorusLinkPositions.stream().toList();
        for (int i = 0; i < posList.size(); i++) {
            writeView.putString("elem_" + i,
                    posList.get(i).dimension().getValue().toString()
                            + "::"
                            + posList.get(i).pos().getX()
                            + ","
                            + posList.get(i).pos().getY()
                            + ","
                            + posList.get(i).pos().getZ());
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

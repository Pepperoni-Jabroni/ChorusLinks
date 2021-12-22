package pepjebs.choruslinks.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import pepjebs.choruslinks.ChorusLinksMod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChorusLinkLocationList extends PersistentState {
    private static List<Pair<String, BlockPos>> locations = new ArrayList<>();

    public ChorusLinkLocationList() {}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        try {
            nbt.putByteArray(ChorusLinksMod.MOD_ID+":contents", serialize(locations));
            return nbt;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChorusLinkLocationList readNbt(NbtCompound nbt) {
        if (nbt == null || !nbt.contains(ChorusLinksMod.MOD_ID+"contents")) return null;
        try {
            locations =
                    (List<Pair<String, BlockPos>>) deserialize(nbt.getByteArray(ChorusLinksMod.MOD_ID+"contents"));
            return this;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChorusLinkLocationList createState() {
        return this;
    }

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

}

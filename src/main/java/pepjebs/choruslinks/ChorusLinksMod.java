package pepjebs.choruslinks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ChorusLinksMod implements ModInitializer {

    public static String MOD_ID = "chorus_links";

    @Override
    public void onInitialize() {
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "golden_chorus_fruit"),
                new Item(new Item.Settings().group(ItemGroup.FOOD)));
        Block chorus_link = Registry.register(
                Registry.BLOCK,
                new Identifier(MOD_ID, "chorus_link"),
                new Block(FabricBlockSettings.of(Material.METAL)));
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "chorus_link"),
                new BlockItem(chorus_link, new Item.Settings().group(ItemGroup.DECORATIONS)));
    }
}

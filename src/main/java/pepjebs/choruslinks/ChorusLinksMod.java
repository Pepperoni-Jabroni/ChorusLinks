package pepjebs.choruslinks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.block.entity.ChorusLinkBlockEntity;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

public class ChorusLinksMod implements ModInitializer {

    public static String MOD_ID = "chorus_links";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static BlockEntityType<ChorusLinkBlockEntity> CHORUS_LINK_BLOCK_ENTITY_TYPE = null;

    @Override
    public void onInitialize() {
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "golden_chorus_fruit"),
                new GoldenChorusFruitItem(new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.RARE), 8));
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "enchanted_golden_chorus_fruit"),
                new GoldenChorusFruitItem(new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.EPIC), 16));

        Block chorus_link = Registry.register(
                Registry.BLOCK,
                new Identifier(MOD_ID, "chorus_link"),
                new ChorusLinkBlock(FabricBlockSettings.of(Material.METAL)));
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "chorus_link"),
                new BlockItem(chorus_link, new Item.Settings().group(ItemGroup.DECORATIONS)));
        CHORUS_LINK_BLOCK_ENTITY_TYPE = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "chorus_link_type"),
                BlockEntityType.Builder.create(ChorusLinkBlockEntity::new, chorus_link).build(null));
    }
}

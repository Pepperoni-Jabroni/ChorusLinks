package pepjebs.choruslinks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
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
import pepjebs.choruslinks.config.ChorusLinksConfig;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

public class ChorusLinksMod implements ModInitializer {

    public static final String MOD_ID = "chorus_links";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static ChorusLinksConfig CONFIG = null;

    public static BlockEntityType<ChorusLinkBlockEntity> CHORUS_LINK_BLOCK_ENTITY_TYPE;

    @Override
    public void onInitialize() {
        AutoConfig.register(ChorusLinksConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ChorusLinksConfig.class).getConfig();

        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "golden_chorus_fruit"),
                new GoldenChorusFruitItem(
                        new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.RARE),
                        CONFIG.goldenChorusFruitRadiusMultiplier));
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "enchanted_golden_chorus_fruit"),
                new GoldenChorusFruitItem(
                        new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.EPIC),
                        CONFIG.enchantedGoldenChorusFruitRadiusMultiplier));

        Block chorus_link = Registry.register(
                Registry.BLOCK,
                new Identifier(MOD_ID, "chorus_link"),
                new ChorusLinkBlock(
                        FabricBlockSettings
                                .of(Material.METAL)
                                .hardness(3.5f)
                                .requiresTool()));
        Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, "chorus_link"),
                new BlockItem(chorus_link, new Item.Settings().group(ItemGroup.DECORATIONS)));
        CHORUS_LINK_BLOCK_ENTITY_TYPE = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "chorus_link_type"),
                FabricBlockEntityTypeBuilder.create(ChorusLinkBlockEntity::new, chorus_link).build(null));
    }
}

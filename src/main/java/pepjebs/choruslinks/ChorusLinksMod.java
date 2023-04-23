package pepjebs.choruslinks;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.block.ChorusLinkLocationsComponent;
import pepjebs.choruslinks.config.ChorusLinksConfig;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

public class ChorusLinksMod implements ModInitializer {

    public static final String MOD_ID = "chorus_links";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static ChorusLinksConfig CONFIG = null;

    public static final ComponentKey<ChorusLinkLocationsComponent> LINK_LOCATIONS_KEY =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                    new Identifier("chorus_links", "link_locations"),
                    ChorusLinkLocationsComponent.class);

    @Override
    public void onInitialize() {
        AutoConfig.register(ChorusLinksConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ChorusLinksConfig.class).getConfig();

        Item gcf = Registry.register(
                Registries.ITEM,
                new Identifier(MOD_ID, "golden_chorus_fruit"),
                new GoldenChorusFruitItem(
                        new Item.Settings().rarity(Rarity.RARE)));
        Item egcf = Registry.register(
                Registries.ITEM,
                new Identifier(MOD_ID, "enchanted_golden_chorus_fruit"),
                new GoldenChorusFruitItem(
                        new Item.Settings().rarity(Rarity.EPIC).maxDamage(9)));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.add(gcf);
            content.add(egcf);
        });

        Block chorus_link = Registry.register(
                Registries.BLOCK,
                new Identifier(MOD_ID, "chorus_link"),
                new ChorusLinkBlock(
                        FabricBlockSettings
                                .of(Material.METAL)
                                .hardness(3.5f)
                                .requiresTool()));
        Item cl = Registry.register(
                Registries.ITEM,
                new Identifier(MOD_ID, "chorus_link"),
                new BlockItem(chorus_link, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.add(cl);
        });
    }
}

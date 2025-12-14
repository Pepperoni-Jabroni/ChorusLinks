package pepjebs.choruslinks;

import com.mojang.serialization.Codec;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import pepjebs.choruslinks.block.ChorusLinkBlock;
import pepjebs.choruslinks.block.ChorusLinkBlockEntity;
import pepjebs.choruslinks.block.ChorusLinkLocationsComponent;
import pepjebs.choruslinks.config.ChorusLinksConfig;
import pepjebs.choruslinks.item.GoldenChorusFruitItem;

public class ChorusLinksMod implements ModInitializer {

    public static final String MOD_ID = "chorus_links";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static ChorusLinksConfig CONFIG = null;

    public static BlockEntityType<ChorusLinkBlockEntity> CHORUS_LINK_ENTITY_TYPE;
    public static final ComponentKey<ChorusLinkLocationsComponent> LINK_LOCATIONS_KEY =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                    Identifier.of("chorus_links", "link_locations"),
                    ChorusLinkLocationsComponent.class);

    public static final ComponentType<String> GOLDEN_CHORUS_BIND_DIM_TAG = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(MOD_ID, GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_DIM_TAG),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
    public static final ComponentType<String> GOLDEN_CHORUS_BIND_POS_TAG = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(MOD_ID, GoldenChorusFruitItem.GOLDEN_CHORUS_BIND_POS_TAG),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    @Override
    public void onInitialize() {
        AutoConfig.register(ChorusLinksConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ChorusLinksConfig.class).getConfig();

        RegistryKey<Item> gcf_rk = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "golden_chorus_fruit"));
        Item gcf = Registry.register(
                Registries.ITEM,
                gcf_rk,
                new GoldenChorusFruitItem(
                        new Item.Settings().registryKey(gcf_rk).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.RARE)));
        RegistryKey<Item> egcf_rk = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "enchanted_golden_chorus_fruit"));
        Item egcf = Registry.register(
                Registries.ITEM,
                egcf_rk,
                new GoldenChorusFruitItem(
                        new Item.Settings().registryKey(egcf_rk).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.EPIC).maxDamage(9)));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addAfter(Items.CHORUS_FRUIT, gcf);
            content.addAfter(gcf, egcf);
        });

        RegistryKey<Block> cl_b_rk = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "chorus_link"));
        Block chorus_link = Registry.register(
                Registries.BLOCK,
                Identifier.of(MOD_ID, "chorus_link"),
                new ChorusLinkBlock(
                        AbstractBlock.Settings
                                .create()
                                .registryKey(cl_b_rk)
                                .hardness(3.5f)
                                .requiresTool()));
        RegistryKey<Item> cl_i_rk = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "chorus_link"));
        CHORUS_LINK_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(MOD_ID, "chorus_link_type"),
                FabricBlockEntityTypeBuilder.create(ChorusLinkBlockEntity::new, chorus_link).build());
        Item cl = Registry.register(
                Registries.ITEM,
                Identifier.of(MOD_ID, "chorus_link"),
                new BlockItem(chorus_link, new Item.Settings().registryKey(cl_i_rk)));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.add(cl);
        });

        if (CONFIG == null || CONFIG.enableEndCityGoldenChorusSpawn) {
            LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
                if (key.getValue().compareTo(LootTables.END_CITY_TREASURE_CHEST.getValue()) == 0) {
                    tableBuilder.pool(
                            LootPool.builder()
                                    .with(ItemEntry.builder(gcf)
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4)))
                                    )
                                    .rolls(BinomialLootNumberProvider.create(3,.1f))
                                    .build()
                    );
                    tableBuilder.pool(
                            LootPool.builder()
                                    .with(ItemEntry.builder(egcf))
                                    .rolls(BinomialLootNumberProvider.create(3,.05f))
                                    .build()
                    );
                }
            });
        }
    }
}

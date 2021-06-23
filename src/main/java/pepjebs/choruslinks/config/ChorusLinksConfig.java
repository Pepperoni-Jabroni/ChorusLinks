package pepjebs.choruslinks.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import pepjebs.choruslinks.ChorusLinksMod;

@Config(name = ChorusLinksMod.MOD_ID)
public class ChorusLinksConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    @Comment("The effective radius of a Chorus Fruit when searching for the nearest Chorus Link")
    public int baseChorusFruitLinkRadius = 64;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The multiplier to the radius for a Golden Chorus Fruit when searching for the nearest Chorus Link")
    public int goldenChorusFruitRadiusMultiplier = 8;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The multiplier to the radius for an Enchanted Golden Chorus Fruit when searching for the nearest Chorus Link")
    public int enchantedGoldenChorusFruitRadiusMultiplier = 16;


    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', the Enchanted Golden Chorus Fruit is able to teleport players inter-dimensionally")
    public boolean enableEnchantedInterDimensionTeleport = true;
}

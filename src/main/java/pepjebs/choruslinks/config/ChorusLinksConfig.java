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
    public int baseChorusFruitLinkRadius = 128;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The effective radius multiplier of a Golden Chorus Fruit")
    public double goldenChorusFruitRadiusMultiplier = 4;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The effective radius multiplier of an Enchanted Golden Chorus Fruit")
    public int enchantGoldenChorusFruitRadiusMultiplier = 16;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', the Enchanted Golden Chorus Fruit is able to teleport players inter-dimensionally")
    public boolean enableEnchantedInterDimensionTeleport = true;
}

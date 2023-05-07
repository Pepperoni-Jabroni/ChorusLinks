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
    @Comment("The effective radius multiplier of a Golden Chorus Fruit. Use -1 for inf radius")
    public int goldenChorusFruitRadiusMultiplier = -1;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The effective radius multiplier of an Enchanted Golden Chorus Fruit. Use -1 for inf radius")
    public int enchantGoldenChorusFruitRadiusMultiplier = -1;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', the Enchanted Golden Chorus Fruit is able to teleport players inter-dimensionally")
    public boolean enableEnchantedInterDimensionTeleport = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', Chorus Links will not be chosen for teleportation if strongly powered by Redstone")
    public boolean enableRedstonePowerDeselection = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', the next closest Chorus Link is chosen in cases of the first being obstructed (Else default teleport)")
    public boolean enableObstructionReselection = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If 'true', End City chests will potentially spawn Golden Chorus Fruits")
    public boolean enableEndCityGoldenChorusSpawn = true;
}

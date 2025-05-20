package xyz.numbar.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "numbar")
public class NumbarConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings itemBarSettings =
        new DisplaySettings(true, DisplayCorner.BOTTOM_RIGHT, true, TextMode.SMALL);

    @ConfigEntry.Gui.CollapsibleObject
    public CooldownDisplaySettings cooldownSettings =
        new CooldownDisplaySettings(true, DisplayCorner.TOP_RIGHT, false, TextMode.SMALL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings bossbarSettings =
            new DisplaySettings(true, DisplayCorner.CENTER, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings xpSettings =
            new DisplaySettings(true, DisplayCorner.BOTTOM, true, TextMode.SMALL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings healthSettings =
            new DisplaySettings(false, DisplayCorner.BOTTOM_LEFT, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings armorSettings =
            new DisplaySettings(false, DisplayCorner.TOP_LEFT, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings hungerSettings =
            new DisplaySettings(false, DisplayCorner.BOTTOM_RIGHT, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings breathSettings =
            new DisplaySettings(false, DisplayCorner.TOP_RIGHT, true, TextMode.NORMAL);

    public static NumbarConfig get() {
        return AutoConfig.getConfigHolder(NumbarConfig.class).getConfig();
    }

    public void set() {
        AutoConfig.getConfigHolder(NumbarConfig.class).setConfig(this);
    }
}

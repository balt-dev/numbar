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
        new CooldownDisplaySettings(true, DisplayCorner.TOP_RIGHT, false, TextMode.SMALL, TimerMode.SECONDS);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings bossbarSettings =
            new DisplaySettings(true, DisplayCorner.CENTER, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings xpSettings =
            new DisplaySettings(true, DisplayCorner.TOP, true, TextMode.SMALL, 0, 2);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings healthSettings =
            new DisplaySettings(true, DisplayCorner.LEFT, true, TextMode.NORMAL, 0, 1);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings armorSettings =
            new DisplaySettings(true, DisplayCorner.LEFT, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings hungerSettings =
            new DisplaySettings(true, DisplayCorner.RIGHT, true, TextMode.NORMAL);

    @ConfigEntry.Gui.CollapsibleObject
    public DisplaySettings breathSettings =
            new DisplaySettings(true, DisplayCorner.RIGHT, true, TextMode.NORMAL);

    public static NumbarConfig get() {
        return AutoConfig.getConfigHolder(NumbarConfig.class).getConfig();
    }

    public void set() {
        AutoConfig.getConfigHolder(NumbarConfig.class).setConfig(this);
    }
}

package xyz.numbar.config;

public class CooldownDisplaySettings extends DisplaySettings {
    public TimerMode timerMode;

    public CooldownDisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode, TimerMode timerMode) {
        super(enabled, displayCorner, hasShadow, textMode);
        this.timerMode = timerMode;
    }

    public CooldownDisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode) {
        super(enabled, displayCorner, hasShadow, textMode);
    }
}



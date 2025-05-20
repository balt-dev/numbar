package xyz.numbar.config;

// Ideally this would extend DisplaySettings but autoconfig doesn't like that sooooo
public class CooldownDisplaySettings {
    public boolean enabled = true;
    public DisplayCorner displayCorner = DisplayCorner.CENTER;
    public boolean hasShadow = true;
    public TextMode textMode = TextMode.NORMAL;
    public int xOffset = 0;
    public int yOffset = 0;
    public TimerMode timerMode = TimerMode.SECONDS;

    public CooldownDisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode, TimerMode timerMode) {
        this.timerMode = timerMode;
        this.enabled = enabled;
        this.displayCorner = displayCorner;
        this.hasShadow = hasShadow;
        this.textMode = textMode;
    }

    public DisplaySettings hackyCast() {
        return new DisplaySettings(enabled, displayCorner, hasShadow, textMode, xOffset, yOffset);
    }
}



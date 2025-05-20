package xyz.numbar.config;

public class DisplaySettings {
    public boolean enabled = true;
    public DisplayCorner displayCorner = DisplayCorner.CENTER;
    public boolean hasShadow = true;
    public TextMode textMode = TextMode.NORMAL;
    public int xOffset = 0;
    public int yOffset = 0;

    public DisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode) {
        this(enabled, displayCorner, hasShadow, textMode, 0, 0);
    }

    public DisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode, int xOffset, int yOffset) {
        this.enabled = enabled;
        this.displayCorner = displayCorner;
        this.hasShadow = hasShadow;
        this.textMode = textMode;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public DisplaySettings copy() {
        return new DisplaySettings(enabled, displayCorner, hasShadow, textMode, xOffset, yOffset);
    }
}

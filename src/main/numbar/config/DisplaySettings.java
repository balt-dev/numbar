package xyz.numbar.config;

public class DisplaySettings {
    public boolean enabled = true;
    public DisplayCorner displayCorner = DisplayCorner.CENTER;
    public boolean hasShadow = true;
    public TextMode textMode = TextMode.NORMAL;

    public DisplaySettings(boolean enabled, DisplayCorner displayCorner, boolean hasShadow, TextMode textMode) {
        this.enabled = enabled;
        this.displayCorner = displayCorner;
        this.hasShadow = hasShadow;
        this.textMode = textMode;
    }
}

package xyz.numbar;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2i;
import xyz.numbar.config.DisplaySettings;
import xyz.numbar.config.TextMode;

public class NumbarGUIHelper {
    public static void drawAnchoredText(DrawContext ctx, TextRenderer textRenderer, String value, int x, int y, int w, int h, int color, DisplaySettings settings) {
        drawAnchoredText(ctx, textRenderer, value, x, y, 199.0f, w, h, color, settings);
    }

    public static void drawAnchoredText(DrawContext ctx, TextRenderer textRenderer, String value, int x, int y, float z, int w, int h, int color, DisplaySettings settings) {
        MatrixStack matrices = ctx.getMatrices();
        matrices.push();
        matrices.translate(0.0F, 0.0F, z);

        settings.textMode = settings.textMode == null ? TextMode.NORMAL : settings.textMode;

        String formattedValue = settings.textMode.format(value);

        y += settings.textMode.yOffset();

        int textWidth = textRenderer.getWidth(formattedValue);
        int textHeight = textRenderer.fontHeight;

        Vector2i anchor = settings.displayCorner.getPosition(x, y, w - textWidth, h - textHeight);
        ctx.drawText(textRenderer, formattedValue, anchor.x, anchor.y, color, settings.hasShadow);
        matrices.pop();
    }
}

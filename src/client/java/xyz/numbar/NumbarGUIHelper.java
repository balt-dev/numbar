package xyz.numbar;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import xyz.numbar.config.DisplaySettings;
import xyz.numbar.config.TextMode;

import java.util.Objects;
import java.util.Optional;

public class NumbarGUIHelper {
    public static void drawAnchoredText(DrawContext ctx, TextRenderer textRenderer, Text value, int x, int y, int w, int h, int color, DisplaySettings settings) {
        drawAnchoredText(ctx, textRenderer, value, x, y, 199.0f, w, h, color, settings);
    }

    public static void drawAnchoredText(DrawContext ctx, TextRenderer textRenderer, Text value, int x, int y, float z, int w, int h, int color, DisplaySettings settings) {
        if (!settings.enabled) return;
        MatrixStack matrices = ctx.getMatrices();
        matrices.push();
        matrices.translate(0.0F, 0.0F, z);

        settings.textMode = settings.textMode == null ? TextMode.NORMAL : settings.textMode;

        MutableText newText = Text.empty();

        value.visit((style, content) -> {
            MutableText segment = Text.literal(settings.textMode.format(content));
            segment.setStyle(style);
            newText.append(segment);
            return Optional.empty();
        }, Style.EMPTY);

        y += settings.textMode.yOffset();

        x += settings.xOffset;
        y += settings.yOffset;

        int textWidth = textRenderer.getWidth(newText);
        int textHeight = textRenderer.fontHeight;

        Vector2i anchor = settings.displayCorner.getPosition(x, y, w - textWidth, h - textHeight);
        ctx.drawText(textRenderer, newText, anchor.x, anchor.y, color, settings.hasShadow);
        matrices.pop();
    }
}

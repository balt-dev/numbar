package xyz.numbar.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.numbar.NumbarGUIHelper;
import xyz.numbar.config.DisplaySettings;
import xyz.numbar.config.NumbarConfig;


@Mixin(Bar.class)
public interface BarMixin {

	@Inject(at = @At("HEAD"), method = "drawExperienceLevel(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V")
	private static void drawXPPoints(DrawContext context, TextRenderer textRenderer, int level, CallbackInfo ci) {
		int XP_BAR_WIDTH = 182;
		int XP_BAR_HEIGHT = 5;

		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;
		if (client.interactionManager.hasExperienceBar() && player.experienceLevel > 0) {
			int x = (context.getScaledWindowWidth() - (XP_BAR_WIDTH - 2)) / 2;
			int y = context.getScaledWindowHeight() - 31;

			DisplaySettings newSettings = NumbarConfig.get().xpSettings.copy();
			boolean border = newSettings.hasShadow;
			newSettings.hasShadow = false;
			String formattedText = "%d / %d".formatted((int) Math.floor(player.experienceProgress * player.getNextLevelExperience()), player.getNextLevelExperience());
			Text text = Text.literal(formattedText);
			if (border) {
				NumbarGUIHelper.drawAnchoredText(context, client.textRenderer, text, x-1, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, client.textRenderer, text, x+1, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, client.textRenderer, text, x, y-1, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, client.textRenderer, text, x, y+1, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
			}

			NumbarGUIHelper.drawAnchoredText(
					context, MinecraftClient.getInstance().textRenderer, text, x, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF80FF20, newSettings
			);
		}
	}
}
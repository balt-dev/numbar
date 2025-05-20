package xyz.numbar.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profilers;
import org.joml.Vector2i;
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


@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Shadow @Final private static Identifier FOOD_FULL_TEXTURE;
	@Shadow @Final private static Identifier FOOD_FULL_HUNGER_TEXTURE;
	@Shadow @Final private static Identifier FOOD_EMPTY_TEXTURE;
	private static final int XP_BAR_WIDTH = 182;
	private static final int XP_BAR_HEIGHT = 5;

	@Inject(at = @At("HEAD"), method = "renderExperienceLevel(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
	private void drawXPPoints(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		InGameHud self = (InGameHud) (Object) this;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (self.shouldRenderExperience() && player.experienceLevel > 0) {
			int x = (context.getScaledWindowWidth() - (XP_BAR_WIDTH - 2)) / 2;
			int y = context.getScaledWindowHeight() - 31;

			DisplaySettings newSettings = NumbarConfig.get().xpSettings.copy();
			boolean border = newSettings.hasShadow;
			newSettings.hasShadow = false;
			String formattedText = "%d / %d".formatted((int) Math.floor(player.experienceProgress * player.getNextLevelExperience()), player.getNextLevelExperience());
			Text text = Text.literal(formattedText);
			if (border) {
				NumbarGUIHelper.drawAnchoredText(context, MinecraftClient.getInstance().textRenderer, text, x-1, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, MinecraftClient.getInstance().textRenderer, text, x+1, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, MinecraftClient.getInstance().textRenderer, text, x, y-1, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
				NumbarGUIHelper.drawAnchoredText(context, MinecraftClient.getInstance().textRenderer, text, x, y+1, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF000000, newSettings);
			}

			NumbarGUIHelper.drawAnchoredText(
				context, MinecraftClient.getInstance().textRenderer, text, x, y, XP_BAR_WIDTH, XP_BAR_HEIGHT, 0xFF80FF20, newSettings
			);
		}
	}

	@Unique
	private static final int STATUS_BAR_WIDTH = 80;
	@Unique
	private static final int STATUS_BAR_HEIGHT = 9;

	private static final Identifier ARMOR_FULL_TEXTURE = Identifier.ofVanilla("hud/armor_full");

	@Inject(at = @At("HEAD"), method = "renderArmor(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIII)V", cancellable = true)
	private static void drawArmor(DrawContext context, PlayerEntity player, int y, int rowOffset, int lineHeight, int x, CallbackInfo ci) {
		DisplaySettings armorSettings = NumbarConfig.get().armorSettings;
		if (!armorSettings.enabled) { return; }
		ci.cancel();

		int armorPoints = player.getArmor();
		if (armorPoints > 0) {
			String formattedText = "   %d".formatted(armorPoints, 20);

			int yOffset = y - (rowOffset - 1) * lineHeight - 10;

			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

			NumbarGUIHelper.drawAnchoredText(
				context, MinecraftClient.getInstance().textRenderer, Text.literal(formattedText), x, yOffset,
				STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, 0xFFC0C0C0, armorSettings
			);

			x += armorSettings.xOffset;
			yOffset += armorSettings.yOffset;

			int textWidth = textRenderer.getWidth(formattedText);
			int textHeight = textRenderer.fontHeight;

			Vector2i anchor = armorSettings.displayCorner.getPosition(
					x, yOffset - 1, STATUS_BAR_WIDTH - textWidth, STATUS_BAR_HEIGHT - textHeight
			);

			context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_FULL_TEXTURE, anchor.x, anchor.y, 9, 9);
		}
	}

	@ModifyVariable(
			method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
			at = @At(
					value = "STORE",
					ordinal = 0
			),
			name = "p"
	)
	private int alignHearts(int originalPValue) {
		return NumbarConfig.get().healthSettings.enabled ? 1 : originalPValue;
	}

	private static final Identifier VEHICLE_FULL_HEART_TEXTURE = Identifier.ofVanilla("hud/heart/vehicle_full");

	@Inject(at = @At("HEAD"), method = "renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V", cancellable = true)
	private void renderHealthBar(
			DrawContext context, PlayerEntity player, int x, int y, int lines,
			int regeneratingHeartIndex, float maxHealth, int lastHealth,
			int health, int absorption, boolean blinking, CallbackInfo ci
	) {
		DisplaySettings healthSettings = NumbarConfig.get().healthSettings;
		if (!healthSettings.enabled) { return; }
		ci.cancel();

		health = (int) player.getHealth();

		String formattedText = "   %d/%d".formatted(health, (int) maxHealth);
		Text text = Text.literal(formattedText).formatted(Formatting.RED);
		if (absorption > 0) {
			Text absText = Text.literal(" +%d".formatted(absorption)).withColor(0xFFF5C211);
			text = text.copy().append(absText);
		}

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		NumbarGUIHelper.drawAnchoredText(
				context, textRenderer, text, x, y,
				STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, 0xFFFFFFFF, healthSettings
		);

		x += healthSettings.xOffset;
		y += healthSettings.yOffset;

		int textWidth = textRenderer.getWidth(text);
		int textHeight = textRenderer.fontHeight;

		InGameHud self = (InGameHud) (Object) this;

		InGameHud.HeartType heartType = InGameHud.HeartType.fromPlayerState(player);

		Vector2i anchor = healthSettings.displayCorner.getPosition(
				x, y, STATUS_BAR_WIDTH - textWidth, STATUS_BAR_HEIGHT - textHeight
		);

		self.drawHeart(context, InGameHud.HeartType.CONTAINER, anchor.x, anchor.y - 1, player.getWorld().getLevelProperties().isHardcore(), false, false);
		self.drawHeart(context, heartType, anchor.x, anchor.y - 1, player.getWorld().getLevelProperties().isHardcore(), false, false);
	}

	@Inject(at = @At("HEAD"), method = "renderFood(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;II)V", cancellable = true)
	private void renderHungerBar(
			DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci
	) {
		DisplaySettings hungerSettings = NumbarConfig.get().hungerSettings;
		if (!hungerSettings.enabled) { return; }
		ci.cancel();

		HungerManager hungerManager = player.getHungerManager();
		int foodLevel = hungerManager.getFoodLevel();
		Identifier drawnTexture = player.hasStatusEffect(StatusEffects.HUNGER)
				? FOOD_FULL_HUNGER_TEXTURE : FOOD_FULL_TEXTURE;

		int saturationLevel = (int) hungerManager.getSaturationLevel();

		String formattedText = "   %d/%d (%d)".formatted(foodLevel, (int) 20, saturationLevel);
		Text text = Text.literal(formattedText).formatted();

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		int left = right - STATUS_BAR_WIDTH;

		NumbarGUIHelper.drawAnchoredText(
				context, textRenderer, text, left, top,
				STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, 0xFFFFFFFF, hungerSettings
		);

		int textWidth = textRenderer.getWidth(text);

		Vector2i anchor = hungerSettings.displayCorner.getPosition(
			left, top, STATUS_BAR_WIDTH - textWidth, STATUS_BAR_HEIGHT - textRenderer.fontHeight
		);

		context.drawGuiTexture(RenderLayer::getGuiTextured, FOOD_EMPTY_TEXTURE, anchor.x, anchor.y, 9, 9);
		context.drawGuiTexture(RenderLayer::getGuiTextured, drawnTexture, anchor.x, anchor.y, 9, 9);
	}

	@Unique
	private static final Identifier AIR_TEXTURE = Identifier.ofVanilla("hud/air");

	@Inject(at = @At("HEAD"), method = "renderAirBubbles(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;III)V", cancellable = true)
	private void renderAirBubbles(
			DrawContext context, PlayerEntity player, int heartCount, int top, int right, CallbackInfo ci
	) {
		DisplaySettings breathSettings = NumbarConfig.get().breathSettings;
		if (!breathSettings.enabled) { return; }
		ci.cancel();

		int air = player.getAir();
		int maxAir = player.getMaxAir();
		int clampedAir = Math.clamp(air, 0, maxAir);
		boolean inWater = player.isSubmergedIn(FluidTags.WATER);

		if (inWater || clampedAir < maxAir) {
			String formattedText = "   %d/%d".formatted(clampedAir, maxAir);
			Text text = Text.literal(formattedText).formatted();

			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

			int left = right - STATUS_BAR_WIDTH;
			top += STATUS_BAR_HEIGHT;

			NumbarGUIHelper.drawAnchoredText(
					context, textRenderer, text, left, top,
					STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, 0xFFFFFFFF, breathSettings
			);

			int textWidth = textRenderer.getWidth(text);

			Vector2i anchor = breathSettings.displayCorner.getPosition(
					left, top, STATUS_BAR_WIDTH - textWidth, STATUS_BAR_HEIGHT - textRenderer.fontHeight
			);

			context.drawGuiTexture(RenderLayer::getGuiTextured, AIR_TEXTURE, anchor.x, anchor.y, 9, 9);
		}
	}
}
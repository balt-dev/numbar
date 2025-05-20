package xyz.numbar.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.numbar.NumbarGUIHelper;
import xyz.numbar.config.DisplaySettings;
import xyz.numbar.config.NumbarConfig;
import xyz.numbar.config.TimerMode;


@Mixin(BossBarHud.class)
public class BossbarMixin {
	private static final int BOSSBAR_WIDTH = 182;
	private static final int BOSSBAR_HEIGHT = 5;

	@Inject(at = @At("HEAD"), method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V")
	private void drawBossbar(DrawContext context, int x, int y, BossBar bossBar, CallbackInfo ci) {
		NumbarGUIHelper.drawAnchoredText(
			context,
			MinecraftClient.getInstance().textRenderer,
			Text.literal("%d%%".formatted((int) Math.ceil(bossBar.getPercent() * 100))),
			x, y, BOSSBAR_WIDTH, BOSSBAR_HEIGHT,
			0xFFFFFFFF,
			NumbarConfig.get().bossbarSettings
		);
	}
}
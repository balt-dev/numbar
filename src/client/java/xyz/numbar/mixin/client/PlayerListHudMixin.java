package xyz.numbar.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.NotImplementedException;
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


@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
	@Inject(at = @At("HEAD"), method = "renderLatencyIcon(Lnet/minecraft/client/gui/DrawContext;IIILnet/minecraft/client/network/PlayerListEntry;)V", cancellable = true)
	private void renderPing(
		DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci
	) {
		DisplaySettings settings = NumbarConfig.get().pingSettings;
		if (!settings.enabled) { return; }
		ci.cancel();
		NumbarGUIHelper.drawAnchoredText(
			context,
			MinecraftClient.getInstance().textRenderer,
			Text.literal("%d".formatted(entry.getLatency())),
			x, y,300, width, 8,
			0xFF00FF21,
			settings
		);
	}
}
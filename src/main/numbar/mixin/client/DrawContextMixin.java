package xyz.numbar.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.numbar.config.NumbarConfig;
import xyz.numbar.NumbarGUIHelper;


@Mixin(DrawContext.class)
public class DrawContextMixin {
	private static ThreadLocal<TextRenderer> RENDERER = new ThreadLocal<>();

	@Inject(at = @At("HEAD"), method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
	private void saveTextRenderer(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
		RENDERER.set(textRenderer);
	}

	@Inject(at = @At("RETURN"), method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
	private void removeTextRenderer(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        RENDERER.remove();
	}




	@Inject(at = @At("HEAD"), method = "drawItemBar(Lnet/minecraft/item/ItemStack;II)V", cancellable = true)
	private void drawItemBar(ItemStack stack, int x, int y, CallbackInfo ci) {
		if (!stack.isItemBarVisible()) {
			return;
		}

		NumbarConfig conf = NumbarConfig.get();
		if (conf.itemBarSettings.enabled) {
			// x and y are the top-left of the item in the GUI
			String progress;
			if (stack.getMaxDamage() != 0) {
				progress = String.valueOf(stack.getMaxDamage() - stack.getDamage());
			} else {
				float t = (((float) stack.getItemBarStep()) / 13.0f * 100f);
				if (stack.getItem() instanceof BundleItem) {
					t = BundleItem.getAmountFilled(stack) * 100f;
				}
				progress = "%d%%".formatted((int) t);
			}

			Vector2i offset = conf.itemBarSettings.displayCorner.getPosition(0, 0, 1, 2);
			x += offset.x;
			y += offset.y;

            //noinspection DataFlowIssue
            NumbarGUIHelper.drawAnchoredText(
					((DrawContext) (Object) this),
					RENDERER.get(),
					progress,
					x, y,16, 16,
					ColorHelper.fullAlpha(stack.getItemBarColor()),
					conf.itemBarSettings
			);
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "drawCooldownProgress(Lnet/minecraft/item/ItemStack;II)V", cancellable = true)
	private void drawCooldown(ItemStack stack, int x, int y, CallbackInfo ci) {

		NumbarConfig conf = NumbarConfig.get();
		if (conf.itemBarSettings.enabled) {
			MinecraftClient client = MinecraftClient.getInstance();

			ClientPlayerEntity clientPlayerEntity = client.player;
			float f = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack, client.getRenderTickCounter().getTickProgress(true));
			if (f <= 0.0) { return; }

			// We estimate the cooldown in ticks using float stuff
			float current = clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack, client.getRenderTickCounter().getTickProgress(true));
			float next = clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack, client.getRenderTickCounter().getTickProgress(true) + 1f);
			float delta = next - current;

			// Delta now contains how much the cooldown changes in one tick
			// We do some algebra to find how much is left
			float ticksLeft = delta == 0.0f ? 0f : current / -delta;

			// x and y are the top-left of the item in the GUI
			String progress = conf.cooldownSettings.timerMode.formatLeft(ticksLeft);

			//noinspection DataFlowIssue
			NumbarGUIHelper.drawAnchoredText(
					((DrawContext) (Object) this),
					RENDERER.get(),
					progress,
					x, y,300, 16, 16,
					0x80FFFFFF,
					conf.cooldownSettings
			);
			ci.cancel();
		}
	}


}
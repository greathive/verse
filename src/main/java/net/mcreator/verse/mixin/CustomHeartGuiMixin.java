package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.VerseModVariables;

@Mixin(Gui.class)
public abstract class CustomHeartGuiMixin {
	@Shadow
	private RandomSource random;

	@Shadow
	protected abstract void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean blinking, boolean half);

	@Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
	private void customHeartRendering(GuiGraphics guiGraphics, Player player, int x, int y, int rowHeight, int regenerationHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean blinking, CallbackInfo ci) {
		// Get the life string variable
		String life = player.getData(VerseModVariables.PLAYER_VARIABLES).life;
		
		// If life is null, empty, or player has certain effects, use vanilla hearts
		if (life == null || life.isEmpty()) {
			return;
		}
		
		// Check if player has poison, wither, or absorption effects - use vanilla hearts if they do
		if (player.hasEffect(MobEffects.POISON) || player.hasEffect(MobEffects.WITHER) || player.hasEffect(MobEffects.ABSORPTION)) {
			return;
		}
		
		// Check if textures exist for this life type
		if (!texturesExist(life)) {
			System.err.println("[VERSE] Heart textures not found for life type: " + life + ", using vanilla hearts");
			return;
		}
		
		// Custom heart rendering
		boolean hardcore = player.level().getLevelData().isHardcore();
		int maxHearts = Mth.ceil((double) maxHealth / 2.0);
		int absorptionHearts = Mth.ceil((double) absorptionAmount / 2.0);
		int totalHearts = maxHearts * 2;
		
		for (int heartIndex = maxHearts + absorptionHearts - 1; heartIndex >= 0; heartIndex--) {
			int row = heartIndex / 10;
			int col = heartIndex % 10;
			int heartX = x + col * 8;
			int heartY = y - row * rowHeight;
			
			if (currentHealth + absorptionAmount <= 4) {
				heartY += this.random.nextInt(2);
			}
			
			if (heartIndex < maxHearts && heartIndex == regenerationHeartIndex) {
				heartY -= 2;
			}
			
			this.renderHeart(guiGraphics, Gui.HeartType.CONTAINER, heartX, heartY, hardcore, false, false);
			
			int heartValue = heartIndex * 2;
			boolean isAbsorptionHeart = heartIndex >= maxHearts;
			
			if (isAbsorptionHeart) {
				// Handle absorption hearts
				int absorptionValue = heartValue - totalHearts;
				if (absorptionValue < absorptionAmount) {
					boolean isHalfAbsorption = absorptionValue + 1 == absorptionAmount;
					this.renderHeart(guiGraphics, Gui.HeartType.ABSORBING, heartX, heartY, hardcore, false, isHalfAbsorption);
				}
			}
			
			// Handle blinking hearts
			if (blinking && heartValue < displayHealth) {
				boolean isHalfBlinking = heartValue + 1 == displayHealth;
				this.renderCustomHeart(guiGraphics, heartX, heartY, hardcore, true, isHalfBlinking, false, life);
			}
			
			// Handle normal hearts
			if (heartValue < currentHealth) {
				boolean isHalfHeart = heartValue + 1 == currentHealth;
				this.renderCustomHeart(guiGraphics, heartX, heartY, hardcore, false, isHalfHeart, false, life);
			}
		}
		
		ci.cancel(); // Prevent vanilla rendering
	}

	private boolean texturesExist(String life) {
		// Check if all required textures exist for this life type
		try {
			ResourceLocation fullTexture = ResourceLocation.parse("verse:textures/screens/" + life + "_full.png");
			ResourceLocation halfTexture = ResourceLocation.parse("verse:textures/screens/" + life + "_half.png");
			ResourceLocation fullBlinkTexture = ResourceLocation.parse("verse:textures/screens/" + life + "_full_blinking.png");
			ResourceLocation halfBlinkTexture = ResourceLocation.parse("verse:textures/screens/" + life + "_half_blinking.png");
			
			var resourceManager = Minecraft.getInstance().getResourceManager();
			
			return resourceManager.getResource(fullTexture).isPresent() 
				&& resourceManager.getResource(halfTexture).isPresent()
				&& resourceManager.getResource(fullBlinkTexture).isPresent()
				&& resourceManager.getResource(halfBlinkTexture).isPresent();
		} catch (Exception e) {
			return false;
		}
	}

	private void renderCustomHeart(GuiGraphics guiGraphics, int x, int y, boolean hardcore, boolean blinking, boolean half, boolean isAbsorption, String life) {
		ResourceLocation texture;
		
		if (blinking) {
			texture = half ? 
				ResourceLocation.parse("verse:textures/screens/" + life + "_half_blinking.png") : 
				ResourceLocation.parse("verse:textures/screens/" + life + "_full_blinking.png");
		} else {
			texture = half ? 
				ResourceLocation.parse("verse:textures/screens/" + life + "_half.png") : 
				ResourceLocation.parse("verse:textures/screens/" + life + "_full.png");
		}
		
		guiGraphics.blit(texture, x, y, 0, 0, 9, 9, 9, 9);
	}
}
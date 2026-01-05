package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

import net.mcreator.verse.init.VerseModMobEffects;

@Mixin(MultiPlayerGameMode.class)
public class PreventBlockInteractionMixin {
	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void preventItemUse(net.minecraft.client.player.LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
		// Uncomment if you have guardbroken effect:
		if (player != null && player.hasEffect(VerseModMobEffects.GUARDBROKEN)) {
			cir.setReturnValue(InteractionResult.FAIL);
		}
	}
}
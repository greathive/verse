package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.init.VerseModMobEffects;

public class AgilityTrainingOnEffectActiveTickProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        double speed = 0;
        
        if (entity instanceof Player player) {
            // Only do heavy operations every 80 ticks (4 seconds)
            if (entity.tickCount % 80 == 0) {
                // Count active ankle weights
                int activeCount = 0;
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(BuiltInRegistries.ITEM.get(ResourceLocation.parse("verse:ankle_weights"))) 
                        && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("active")) {
                        activeCount += stack.getCount();
                    }
                }
                
                // Only proceed if there are active ankle weights
                if (activeCount > 0) {
                    // Refresh effect with current active count
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                        _entity.addEffect(new MobEffectInstance(VerseModMobEffects.AGILITY_TRAINING, 100, activeCount - 1, false, false));
                    }
                } else {
                    // Remove effect if no active ankle weights
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                        _entity.removeEffect(VerseModMobEffects.AGILITY_TRAINING);
                    }
                }
            }
            
            // Apply agility exp gain every ~2.5 seconds (50 ticks)
            if ((float) (entity.tickCount / 49.5) == Math.floor(entity.tickCount / 49.5)) {
                speed = player.getDeltaMovement().length();
                
                // Damage all active ankle weights
                if (world instanceof ServerLevel _level) {
                    for (ItemStack stack : player.getInventory().items) {
                        if (stack.is(BuiltInRegistries.ITEM.get(ResourceLocation.parse("verse:ankle_weights"))) 
                            && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("active")) {
                            stack.hurtAndBreak(1, _level, null, _stkprov -> {
                            });
                        }
                    }
                }
                
                // Grant agility experience
                VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
                _vars.aglExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).aglExp + Mth.nextInt(RandomSource.create(), 
                    (int) ((speed / 20) * 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)),
                    (int) (speed * 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)));
                _vars.markSyncDirty();
            }
        }
    }
}
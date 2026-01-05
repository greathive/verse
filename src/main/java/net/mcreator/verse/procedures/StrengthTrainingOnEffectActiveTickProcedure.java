package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.verse.network.VerseModVariables;

public class StrengthTrainingOnEffectActiveTickProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        double maxvalue = 0;
        if (!(((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) == null) && ((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null).tickCount
                - (((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) instanceof LivingEntity _livEnt ? _livEnt.getLastHurtByMobTimestamp() : 0) < 600) {
            maxvalue = ((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1;
        } else {
            maxvalue = 0;
        }
        {
            VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
            _vars.strExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + Mth.nextInt(RandomSource.create(), 0, (int) maxvalue);
            _vars.markSyncDirty();
        }
        
        // Damage active dumbbells only if player has hit an enemy within the last 30 seconds
        if (entity instanceof Player player) {
            if (!(((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) == null) && ((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null).tickCount
                    - (((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) instanceof LivingEntity _livEnt ? _livEnt.getLastHurtByMobTimestamp() : 0) < 600) {
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(BuiltInRegistries.ITEM.get(ResourceLocation.parse("verse:dumbbell"))) 
                        && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("active")) {
                        if (world instanceof ServerLevel _level) {
                            stack.hurtAndBreak(1, _level, null, _stkprov -> {
                            });
                        }
                    }
                }
            }
        }
    }
}
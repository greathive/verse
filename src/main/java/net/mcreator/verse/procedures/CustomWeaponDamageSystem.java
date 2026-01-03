package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.util.SwingDataLoader;
import net.mcreator.verse.init.VerseModMobEffects;

import java.util.List;

@EventBusSubscriber
public class CustomWeaponDamageSystem {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Only run on server side
        if (player.level().isClientSide()) return;
        if (!(player instanceof ServerPlayer)) return;

        ItemStack mainHand = player.getMainHandItem();

        // Check if this is a valid weapon for our system
        SwingDataLoader.SwingData swingData = SwingDataLoader.getSwingData(mainHand);
        if (swingData == null) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        // Check if we're in an attack
        if (!data.contains("CustomAttackCooldownUntil")) {
            return;
        }

        long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
        long currentTime = player.level().getGameTime();

        // Clear damage flag when cooldown expires
        if (currentTime >= cooldownUntil) {
            data.remove("HasDealtDamage");
            data.remove("AttackStartTime");
            return;
        }

        // Get when this attack started
        if (!data.contains("AttackStartTime")) {
            return;
        }

        long attackStartTime = data.getLong("AttackStartTime");
        long ticksSinceStart = currentTime - attackStartTime;

        // Calculate total swing duration
        double attackSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
        long totalSwingTicks = Math.round(20.0 / attackSpeed);

        // Check if we're at 45% through the swing
        float progressPercent = (float) ticksSinceStart / totalSwingTicks;

        // Damage happens at 40-50% through animation (centered around 45%)
        if (progressPercent < 0.40f || progressPercent > 0.50f) {
            return;
        }

        // Only deal damage once per swing
        if (data.getBoolean("HasDealtDamage")) {
            return;
        }

        // Mark that we've dealt damage this swing
        data.putBoolean("HasDealtDamage", true);

        // Get entity reach attribute
        double reach = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);

        // Get player position and look direction
        Vec3 playerPos = player.position().add(0, player.getEyeHeight() * 0.5, 0);
        Vec3 lookVec = player.getLookAngle();

        // Create a search box around the player
        AABB searchBox = new AABB(playerPos, playerPos).inflate(reach);
        List<Entity> entities = player.level().getEntities(player, searchBox);

        // Damage calculation
        double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        int hitCount = 0;

        for (Entity target : entities) {
            if (!(target instanceof LivingEntity)) {
                continue;
            }

            LivingEntity livingTarget = (LivingEntity) target;

            // Skip if target has parry frame effect
            if (livingTarget.hasEffect(VerseModMobEffects.PARRY_FRAME)) {
                continue;
            }

            // Get vector from player to target (accounting for target height)
            Vec3 targetCenter = target.position().add(0, target.getBbHeight() * 0.5, 0);
            Vec3 toTarget = targetCenter.subtract(playerPos);
            double distanceToTarget = toTarget.length();

            // Check if target is within reach
            if (distanceToTarget > reach) {
                continue;
            }

            // Check for solid blocks between player and target
            ClipContext clipContext = new ClipContext(
                    playerPos,
                    targetCenter,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            );
            BlockHitResult blockHit = player.level().clip(clipContext);

            // If we hit a block before reaching the target, skip this entity
            if (blockHit.getType() == BlockHitResult.Type.BLOCK) {
                Vec3 blockHitPos = blockHit.getLocation();
                double distanceToBlock = playerPos.distanceTo(blockHitPos);
                if (distanceToBlock < distanceToTarget) {
                    continue; // Block is in the way
                }
            }

            // Normalize the direction to target
            Vec3 directionToTarget = toTarget.normalize();

            // Calculate dot product with look direction (yaw/pitch combined)
            double dotProduct = lookVec.dot(directionToTarget);

            // 0.0 = 90 degrees (half circle in front)
            if (dotProduct < 0.0) {
                continue;
            }

            // Reset invincibility frames before dealing damage
            livingTarget.invulnerableTime = 0;

            // Deal damage
            DamageSource damageSource = player.damageSources().playerAttack(player);
            boolean damaged = livingTarget.hurt(damageSource, (float) baseDamage);

            if (damaged) {
                // Knockback in direction of attack
                livingTarget.knockback(0.4, -lookVec.x, -lookVec.z);
                hitCount++;
            }
        }

        // Apply durability damage if we hit something
        if (hitCount > 0 && !mainHand.isEmpty()) {
            mainHand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }
}
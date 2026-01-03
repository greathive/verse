package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundSource;

import net.mcreator.verse.entity.NpcEntity;
import net.mcreator.verse.client.model.Modelnpcmodel;
import net.mcreator.verse.VerseModPlayerAnimationAPI;
import net.mcreator.verse.VerseMod;
import net.mcreator.verse.SwingSpeedAnimationHelper;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Mixin(Modelnpcmodel.class)
public class ModelnpcmodelMixin {
    
    @Shadow
    public ModelPart body;
    @Shadow
    public ModelPart torso;
    @Shadow
    public ModelPart head;
    @Shadow
    public ModelPart right_arm;
    @Shadow
    public ModelPart right_item;
    @Shadow
    public ModelPart left_arm;
    @Shadow
    public ModelPart left_item;
    @Shadow
    public ModelPart right_leg;
    @Shadow
    public ModelPart left_leg;
    
    @Unique
    private String master = null;

    @Inject(method = "setupAnim", at = @At("HEAD"))
    public void beforeSetupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (master == null)
            master = "verse";
        if (!master.equals("verse"))
            return;
            
        // Only apply to NPC entities
        if (!(entity instanceof NpcEntity)) {
            return;
        }
        
        NpcEntity npcEntity = (NpcEntity) entity;
        CompoundTag data = npcEntity.getPersistentData();
        
        // Handle animation progress and timing
        if (ageInTicks <= 0)
            return;
            
        String playingAnimation = data.getString("PlayerCurrentAnimation");
        boolean overrideAnimation = data.getBoolean("OverrideCurrentAnimation");
        
        if (data.getBoolean("ResetPlayerAnimation")) {
            data.remove("ResetPlayerAnimation");
            data.remove("LastAnimationProgress");
            data.remove("PlayedSoundTimes");
            VerseModPlayerAnimationAPI.active_animations.put(npcEntity, null);
        }
        
        if (playingAnimation.isEmpty()) {
            return;
        }
        
        if (overrideAnimation) {
            data.putBoolean("OverrideCurrentAnimation", false);
            data.remove("PlayerAnimationProgress");
            data.remove("LastAnimationProgress");
            data.remove("PlayedSoundTimes");
            VerseModPlayerAnimationAPI.active_animations.put(npcEntity, null);
        }
        
        VerseModPlayerAnimationAPI.PlayerAnimation animation = VerseModPlayerAnimationAPI.active_animations.get(npcEntity);
        if (animation == null) {
            animation = VerseModPlayerAnimationAPI.animations.get(playingAnimation);
            if (animation == null) {
                VerseMod.LOGGER.info("Attempted to play null animation " + playingAnimation + " on NPC");
                return;
            }
            VerseModPlayerAnimationAPI.active_animations.put(npcEntity, animation);
        }
        
        // Get animation speed multiplier based on verse:swing_speed attribute
        float speedMultiplier = SwingSpeedAnimationHelper.getAnimationSpeedMultiplier(npcEntity, playingAnimation);
        
        float animationProgress;
        float lastAnimationProgress = data.getFloat("LastAnimationProgress");
        ListTag playedSoundsTag = data.getList("PlayedSoundTimes", Tag.TAG_FLOAT);
        
        if (!data.contains("PlayerAnimationProgress")) {
            animationProgress = 0f;
            data.putFloat("PlayerAnimationProgress", animationProgress);
            data.putFloat("LastTickTime", ageInTicks);
            data.putFloat("LastAnimationProgress", 0f);
        } else {
            animationProgress = data.getFloat("PlayerAnimationProgress");
            float lastTickTime = data.getFloat("LastTickTime");
            float deltaTime = (ageInTicks - lastTickTime) / 20f;
            
            // Apply speed multiplier to delta time
            animationProgress += deltaTime * speedMultiplier;
            
            data.putFloat("PlayerAnimationProgress", animationProgress);
            data.putFloat("LastTickTime", ageInTicks);
            
            if (animationProgress >= animation.length) {
                if (!animation.hold_on_last_frame && !animation.loop) {
                    data.remove("PlayerCurrentAnimation");
                    data.remove("PlayerAnimationProgress");
                    data.remove("LastAnimationProgress");
                    data.remove("PlayedSoundTimes");
                    data.putBoolean("ResetPlayerAnimation", true);
                    VerseModPlayerAnimationAPI.active_animations.put(npcEntity, null);
                    animationProgress = animation.length;
                } else if (animation.hold_on_last_frame) {
                    data.putFloat("PlayerAnimationProgress", animation.length);
                } else if (animation.loop) {
                    data.remove("PlayerAnimationProgress");
                    data.remove("LastAnimationProgress");
                    data.remove("PlayedSoundTimes");
                }
            }
        }
        
        // Sound effects
        if (!animation.soundEffects.isEmpty()) {
            Set<Float> playedSoundTimes = new HashSet<>();
            for (int i = 0; i < playedSoundsTag.size(); i++) {
                playedSoundTimes.add(playedSoundsTag.getFloat(i));
            }
            
            for (Map.Entry<Float, String> soundEntry : animation.soundEffects.entrySet()) {
                float soundTime = soundEntry.getKey();
                String soundId = soundEntry.getValue();
                
                if (playedSoundTimes.contains(soundTime)) {
                    continue;
                }
                
                boolean shouldPlay = false;
                if (lastAnimationProgress <= animationProgress) {
                    shouldPlay = lastAnimationProgress <= soundTime && animationProgress >= soundTime;
                } else {
                    shouldPlay = lastAnimationProgress <= soundTime || animationProgress >= soundTime;
                }
                
                if (shouldPlay && npcEntity.level() instanceof ClientLevel clientLevel) {
                    clientLevel.playLocalSound(npcEntity.getX(), npcEntity.getY(), npcEntity.getZ(), 
                        BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(soundId)), 
                        SoundSource.NEUTRAL, 1.0F, 1.0F, false);
                    playedSoundsTag.add(FloatTag.valueOf(soundTime));
                }
            }
            data.put("PlayedSoundTimes", playedSoundsTag);
            data.putFloat("LastAnimationProgress", animationProgress);
        }
    }

    @Inject(method = "setupAnim", at = @At("RETURN"))
    public void applyCustomAnimations(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!master.equals("verse"))
            return;
            
        // Only apply to NPC entities
        if (!(entity instanceof NpcEntity)) {
            return;
        }
        
        NpcEntity npcEntity = (NpcEntity) entity;
        CompoundTag data = npcEntity.getPersistentData();
        String playingAnimation = data.getString("PlayerCurrentAnimation");
        
        // Check if this specific entity has an animation assigned to it
        VerseModPlayerAnimationAPI.PlayerAnimation animation = VerseModPlayerAnimationAPI.active_animations.get(npcEntity);
        
        if (playingAnimation.isEmpty() || animation == null) {
            // Animation finished or not playing - explicitly reset rotations to default
            // Body and torso should reset to 0
            if (body.xRot != 0 || body.yRot != 0 || body.zRot != 0) {
                body.xRot = 0;
                body.yRot = 0;
                body.zRot = 0;
            }
            if (torso.xRot != 0 || torso.yRot != 0 || torso.zRot != 0) {
                torso.xRot = 0;
                torso.yRot = 0;
                torso.zRot = 0;
            }
            // Reset arm Y and Z rotations (X is handled by vanilla for walking)
            right_arm.yRot = 0;
            right_arm.zRot = 0;
            left_arm.yRot = 0;
            left_arm.zRot = 0;
            // Reset positions to default
            right_arm.x = -5.0F;
            right_arm.y = -10.0F;
            right_arm.z = 0.0F;
            left_arm.x = 5.0F;
            left_arm.y = -10.0F;
            left_arm.z = 0.0F;
            return;
        }
        
        float animationProgress = data.getFloat("PlayerAnimationProgress");
        
        // Store default positions before applying animation
        float defaultRightArmX = -5.0F;
        float defaultRightArmY = -10.0F;
        float defaultRightArmZ = 0.0F;
        float defaultLeftArmX = 5.0F;
        float defaultLeftArmY = -10.0F;
        float defaultLeftArmZ = 0.0F;
        
        // Apply animations to all bones AFTER vanilla animations
        for (Map.Entry<String, VerseModPlayerAnimationAPI.PlayerBone> entry : animation.bones.entrySet()) {
            String boneName = entry.getKey();
            VerseModPlayerAnimationAPI.PlayerBone bone = entry.getValue();
            ModelPart modelPart = getModelPart(boneName);
            
            if (modelPart == null) {
                if (!boneName.equals("rightItem") && !boneName.equals("leftItem")) {
                    VerseMod.LOGGER.warn("Could not find model part for bone: " + boneName);
                }
                continue;
            }
            
            // Apply rotation - override vanilla
            Vec3 rotation = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.rotations, animationProgress);
            if (rotation != null) {
                modelPart.xRot = (float) Math.toRadians(rotation.x);
                modelPart.yRot = (float) Math.toRadians(rotation.y);
                modelPart.zRot = (float) Math.toRadians(rotation.z);
            }
            
            // Apply position - reset to default first, then add offset
            Vec3 position = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.positions, animationProgress);
            if (position != null) {
                // Reset to default position based on bone
                if (boneName.equals("right_arm")) {
                    modelPart.x = defaultRightArmX + (float) position.x;
                    modelPart.y = defaultRightArmY - (float) position.y;
                    modelPart.z = defaultRightArmZ + (float) position.z;
                } else if (boneName.equals("left_arm")) {
                    modelPart.x = defaultLeftArmX + (float) position.x;
                    modelPart.y = defaultLeftArmY - (float) position.y;
                    modelPart.z = defaultLeftArmZ + (float) position.z;
                } else {
                    // For other bones, just set the position
                    modelPart.x = (float) position.x;
                    modelPart.y = -(float) position.y;
                    modelPart.z = (float) position.z;
                }
            }
            
            // Apply scale
            Vec3 scale = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.scales, animationProgress);
            if (scale != null) {
                modelPart.xScale = (float) scale.x;
                modelPart.yScale = (float) scale.y;
                modelPart.zScale = (float) scale.z;
            }
        }
    }
    
    private ModelPart getModelPart(String boneName) {
        switch (boneName) {
            case "body":
                return body;
            case "torso":
                return torso;
            case "head":
                return head;
            case "right_arm":
                return right_arm;
            case "rightItem":
            case "right_item":
                return right_item;
            case "left_arm":
                return left_arm;
            case "leftItem":
            case "left_item":
                return left_item;
            case "right_leg":
                return right_leg;
            case "left_leg":
                return left_leg;
            default:
                return null;
        }
    }
}
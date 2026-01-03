package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.verse.entity.NpcEntity;
import net.mcreator.verse.VerseModPlayerAnimationAPI;
import net.mcreator.verse.VerseMod;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

@Mixin(value = HumanoidModel.class, priority = 1500)
public abstract class HumanoidAnimationMixin<T extends LivingEntity> {
	private String master = null;
	
	@Unique
	private Map<String, Vec3> storedRotations = new HashMap<>();
	@Unique
	private Map<String, Vec3> storedPositions = new HashMap<>();
	@Unique
	private Map<String, Vec3> storedScales = new HashMap<>();

	@Inject(method = "setupAnim", at = @At(value = "HEAD"))
	public void setupPivot(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (master == null)
			master = "verse";
		if (!master.equals("verse"))
			return;
		
		// Support all zombie variants (Zombie, Husk, Drowned, ZombieVillager, etc.), 
		// all skeleton types, and custom NPC entity
		if (!isAnimatableEntity(entityIn))
			return;
			
		HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
		
		VerseModPlayerAnimationAPI.PlayerAnimation animation = VerseModPlayerAnimationAPI.active_animations.get(entityIn);
		
		if (animation == null)
			return;
		
		// Disable vanilla animations ONLY for bones that are animated
		if (animation.bones.containsKey("left_arm") || animation.bones.containsKey("body") || animation.bones.containsKey("right_arm"))
			model.attackTime = 0;
		model.crouching = false;
		
		// CRITICAL: Set riding to false to prevent bobbing animations
		model.riding = false;
	}

	@Inject(method = "setupAnim", at = @At(value = "RETURN"))
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (ageInTicks <= 0)
			return;
		if (!master.equals("verse")) {
			if (!VerseModPlayerAnimationAPI.animations.isEmpty())
				VerseModPlayerAnimationAPI.animations.clear();
			return;
		}
		
		// Support all zombie variants, all skeleton types, and custom NPC entity
		if (!isAnimatableEntity(entityIn))
			return;
			
		HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
		
		CompoundTag data = entityIn.getPersistentData();
		String playingAnimation = data.getString("PlayerCurrentAnimation");
		boolean overrideAnimation = data.getBoolean("OverrideCurrentAnimation");
		
		// Debug: Log every frame for this entity type to see if NBT data exists
		VerseMod.LOGGER.info("[ANIMATION DEBUG] Entity: " + entityIn.getClass().getSimpleName() + 
							 " | ID: " + entityIn.getId() + 
							 " | Animation: '" + playingAnimation + "'" +
							 " | Has NBT: " + !playingAnimation.isEmpty());
		
		if (data.getBoolean("ResetPlayerAnimation")) {
			data.remove("ResetPlayerAnimation");
			data.remove("LastAnimationProgress");
			data.remove("PlayedSoundTimes");
			VerseModPlayerAnimationAPI.active_animations.put(entityIn, null);
			storedRotations.clear();
			storedPositions.clear();
			storedScales.clear();
		}
		
		if (playingAnimation.isEmpty()) {
			return;
		}
		
		if (overrideAnimation) {
			data.putBoolean("OverrideCurrentAnimation", false);
			data.remove("PlayerAnimationProgress");
			data.remove("LastAnimationProgress");
			data.remove("PlayedSoundTimes");
			VerseModPlayerAnimationAPI.active_animations.put(entityIn, null);
		}
		
		VerseModPlayerAnimationAPI.PlayerAnimation animation = VerseModPlayerAnimationAPI.active_animations.get(entityIn);
		if (animation == null) {
			animation = VerseModPlayerAnimationAPI.animations.get(playingAnimation);
			if (animation == null) {
				VerseMod.LOGGER.info("Attempted to play null animation " + playingAnimation + " on mob");
				return;
			}
			VerseMod.LOGGER.info("Successfully loaded animation: " + playingAnimation + " for " + entityIn.getClass().getSimpleName());
			VerseModPlayerAnimationAPI.active_animations.put(entityIn, animation);
		}
		
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
			animationProgress += deltaTime;
			data.putFloat("PlayerAnimationProgress", animationProgress);
			data.putFloat("LastTickTime", ageInTicks);
			
			if (animationProgress >= animation.length) {
				if (!animation.hold_on_last_frame && !animation.loop) {
					data.remove("PlayerCurrentAnimation");
					data.remove("PlayerAnimationProgress");
					data.remove("LastAnimationProgress");
					data.remove("PlayedSoundTimes");
					data.putBoolean("ResetPlayerAnimation", true);
					VerseModPlayerAnimationAPI.active_animations.put(entityIn, null);
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
				
				if (shouldPlay && entityIn.level() instanceof ClientLevel clientLevel) {
					clientLevel.playLocalSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(), 
						BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(soundId)), 
						SoundSource.NEUTRAL, 1.0F, 1.0F, false);
					playedSoundsTag.add(FloatTag.valueOf(soundTime));
				}
			}
			data.put("PlayedSoundTimes", playedSoundsTag);
			data.putFloat("LastAnimationProgress", animationProgress);
		}
		
		// Apply bone transformations AFTER vanilla - store what we want and forcefully apply
		
		// First pass: calculate and store all our transformations
		Map<ModelPart, Vec3> rotationsToApply = new HashMap<>();
		Map<ModelPart, Vec3> positionsToApply = new HashMap<>();
		Map<ModelPart, Vec3> scalesToApply = new HashMap<>();
		
		for (Map.Entry<String, VerseModPlayerAnimationAPI.PlayerBone> entry : animation.bones.entrySet()) {
			String boneName = entry.getKey();
			VerseModPlayerAnimationAPI.PlayerBone bone = entry.getValue();
			ModelPart modelPart = getModelPart(model, boneName);
			
			if (modelPart == null) {
				if (!boneName.equals("rightItem") && !boneName.equals("leftItem")) {
					VerseMod.LOGGER.warn("Could not find model part for bone: " + boneName);
				}
				continue;
			}
			
			Vec3 rotation = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.rotations, animationProgress);
			if (rotation != null) {
				rotationsToApply.put(modelPart, rotation);
			}
			
			Vec3 position = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.positions, animationProgress);
			if (position != null) {
				positionsToApply.put(modelPart, position);
			}
			
			Vec3 scale = VerseModPlayerAnimationAPI.PlayerBone.interpolate(bone.scales, animationProgress);
			if (scale != null) {
				scalesToApply.put(modelPart, scale);
			}
		}
		
		// Second pass: forcefully apply all transformations
		for (Map.Entry<ModelPart, Vec3> entry : rotationsToApply.entrySet()) {
			ModelPart part = entry.getKey();
			Vec3 rotation = entry.getValue();
			part.xRot = (float) Math.toRadians(rotation.x);
			part.yRot = (float) Math.toRadians(rotation.y);
			part.zRot = (float) Math.toRadians(rotation.z);
			VerseMod.LOGGER.info("Applied rotation: " + rotation);
		}
		
		for (Map.Entry<ModelPart, Vec3> entry : positionsToApply.entrySet()) {
			ModelPart part = entry.getKey();
			Vec3 position = entry.getValue();
			part.x += (float) position.x;
			part.y -= (float) position.y;
			part.z += (float) position.z;
		}
		
		for (Map.Entry<ModelPart, Vec3> entry : scalesToApply.entrySet()) {
			ModelPart part = entry.getKey();
			Vec3 scale = entry.getValue();
			part.xScale = (float) scale.x;
			part.yScale = (float) scale.y;
			part.zScale = (float) scale.z;
		}
	}

	/**
	 * Check if an entity can be animated by this mixin.
	 * Supports: All Zombie variants (Zombie, Husk, Drowned, ZombieVillager, etc.),
	 * all Skeleton variants (Skeleton, Stray, WitherSkeleton, Bogged), and custom NPC entity.
	 */
	@Unique
	private boolean isAnimatableEntity(T entity) {
		// Check if it's any zombie variant (includes Zombie, Husk, Drowned, ZombieVillager)
		if (entity instanceof Zombie) {
			VerseMod.LOGGER.info("Detected Zombie variant: " + entity.getClass().getSimpleName());
			return true;
		}
		
		// Check if it's any skeleton variant (includes Skeleton, Stray, WitherSkeleton, Bogged)
		if (entity instanceof AbstractSkeleton) {
			VerseMod.LOGGER.info("Detected Skeleton variant: " + entity.getClass().getSimpleName());
			return true;
		}
		
		// Check if it's the custom NPC entity
		if (entity instanceof NpcEntity) {
			VerseMod.LOGGER.info("Detected NPC entity");
			return true;
		}
		
		return false;
	}

	private ModelPart getModelPart(HumanoidModel<T> model, String boneName) {
		switch (boneName) {
			case "torso":  // Support both "torso" (Blockbench) and "body" (Minecraft)
			case "body":
				return model.body;
			case "head":
				return model.head;
			case "right_arm":
				return model.rightArm;
			case "left_arm":
				return model.leftArm;
			case "right_leg":
				return model.rightLeg;
			case "left_leg":
				return model.leftLeg;
			default:
				return null;
		}
	}
}
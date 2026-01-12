package net.mcreator.verse.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

import net.mcreator.verse.entity.MegalodauntEntity;
import net.mcreator.verse.client.model.animations.MegalodauntAnimation;
import net.mcreator.verse.client.model.ModelMegalodaunt;

public class MegalodauntRenderer extends MobRenderer<MegalodauntEntity, ModelMegalodaunt<MegalodauntEntity>> {
	public MegalodauntRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(ModelMegalodaunt.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MegalodauntEntity entity) {
		return ResourceLocation.parse("verse:textures/entities/megalodaunt.png");
	}

	private static final class AnimatedModel extends ModelMegalodaunt<MegalodauntEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<MegalodauntEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(MegalodauntEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, MegalodauntAnimation.IDLE_ANIMATION, ageInTicks, 1f);
				this.animateWalk(MegalodauntAnimation.WALK_ANIMATION, limbSwing, limbSwingAmount, 2f, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(MegalodauntEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}
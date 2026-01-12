package net.mcreator.verse.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelMegalodaunt<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("verse", "model_megalodaunt"), "main");
	public final ModelPart main;
	public final ModelPart waist;
	public final ModelPart LeftLeg;
	public final ModelPart LeftKnee;
	public final ModelPart LeftLowerKnee;
	public final ModelPart LeftFoot;
	public final ModelPart LeftToe1;
	public final ModelPart LeftToe3;
	public final ModelPart LeftToe4;
	public final ModelPart RightLeg;
	public final ModelPart RightKnee;
	public final ModelPart RightLowerKnee;
	public final ModelPart RightFoot;
	public final ModelPart RightToe1;
	public final ModelPart RightToe3;
	public final ModelPart RightToe4;
	public final ModelPart upper;
	public final ModelPart neck;
	public final ModelPart head;
	public final ModelPart jaw;
	public final ModelPart neckcoral;
	public final ModelPart LeftArm;
	public final ModelPart LeftElbow;
	public final ModelPart LeftHand;
	public final ModelPart LeftFinger1;
	public final ModelPart LeftFinger2;
	public final ModelPart LeftFinger3;
	public final ModelPart RightArm;
	public final ModelPart RightElbow;
	public final ModelPart RightHand;
	public final ModelPart RightFinger1;
	public final ModelPart RightFinger2;
	public final ModelPart RightFinger3;
	public final ModelPart backcoral;

	public ModelMegalodaunt(ModelPart root) {
		this.main = root.getChild("main");
		this.waist = this.main.getChild("waist");
		this.LeftLeg = this.waist.getChild("LeftLeg");
		this.LeftKnee = this.LeftLeg.getChild("LeftKnee");
		this.LeftLowerKnee = this.LeftKnee.getChild("LeftLowerKnee");
		this.LeftFoot = this.LeftLowerKnee.getChild("LeftFoot");
		this.LeftToe1 = this.LeftFoot.getChild("LeftToe1");
		this.LeftToe3 = this.LeftFoot.getChild("LeftToe3");
		this.LeftToe4 = this.LeftFoot.getChild("LeftToe4");
		this.RightLeg = this.waist.getChild("RightLeg");
		this.RightKnee = this.RightLeg.getChild("RightKnee");
		this.RightLowerKnee = this.RightKnee.getChild("RightLowerKnee");
		this.RightFoot = this.RightLowerKnee.getChild("RightFoot");
		this.RightToe1 = this.RightFoot.getChild("RightToe1");
		this.RightToe3 = this.RightFoot.getChild("RightToe3");
		this.RightToe4 = this.RightFoot.getChild("RightToe4");
		this.upper = this.main.getChild("upper");
		this.neck = this.upper.getChild("neck");
		this.head = this.neck.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.neckcoral = this.neck.getChild("neckcoral");
		this.LeftArm = this.upper.getChild("LeftArm");
		this.LeftElbow = this.LeftArm.getChild("LeftElbow");
		this.LeftHand = this.LeftElbow.getChild("LeftHand");
		this.LeftFinger1 = this.LeftHand.getChild("LeftFinger1");
		this.LeftFinger2 = this.LeftHand.getChild("LeftFinger2");
		this.LeftFinger3 = this.LeftHand.getChild("LeftFinger3");
		this.RightArm = this.upper.getChild("RightArm");
		this.RightElbow = this.RightArm.getChild("RightElbow");
		this.RightHand = this.RightElbow.getChild("RightHand");
		this.RightFinger1 = this.RightHand.getChild("RightFinger1");
		this.RightFinger2 = this.RightHand.getChild("RightFinger2");
		this.RightFinger3 = this.RightHand.getChild("RightFinger3");
		this.backcoral = this.upper.getChild("backcoral");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -15.65F, 1.0F));
		PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 52).addBox(-6.0F, -3.0F, -5.0F, 12.0F, 18.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -11.0F, -1.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition waist = main.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 4.0F));
		PartDefinition LeftLeg = waist.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(46, 52).addBox(-5.0F, -2.0F, -5.0F, 9.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.0F, 0.0F));
		PartDefinition LeftKnee = LeftLeg.addOrReplaceChild("LeftKnee", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -4.0F));
		PartDefinition cube_r2 = LeftKnee.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 96).addBox(-4.75F, 0.0F, 0.0F, 8.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition LeftLowerKnee = LeftKnee.addOrReplaceChild("LeftLowerKnee", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 8.0F));
		PartDefinition cube_r3 = LeftLowerKnee.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(90, 99).addBox(-4.0F, -2.0F, -1.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.5F, -2.5F, 0.1745F, 0.0F, 0.0F));
		PartDefinition LeftFoot = LeftLowerKnee.addOrReplaceChild("LeftFoot", CubeListBuilder.create().texOffs(116, 65).addBox(-4.6047F, 1.9986F, -11.0F, 8.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 4.0F));
		PartDefinition cube_r4 = LeftFoot.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(110, 15).addBox(-3.75F, 0.0F, 0.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.1047F, 0.9986F, -6.0F, -0.0436F, 0.0F, 0.0F));
		PartDefinition LeftToe1 = LeftFoot.addOrReplaceChild("LeftToe1",
				CubeListBuilder.create().texOffs(34, 109).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(122, 91).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.6047F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition LeftToe3 = LeftFoot.addOrReplaceChild("LeftToe3",
				CubeListBuilder.create().texOffs(142, 81).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(122, 95).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.4547F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition LeftToe4 = LeftFoot.addOrReplaceChild("LeftToe4",
				CubeListBuilder.create().texOffs(144, 65).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(142, 87).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.3953F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition RightLeg = waist.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(46, 52).mirror().addBox(-4.0F, -2.0F, -5.0F, 9.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, 0.0F, 0.0F));
		PartDefinition RightKnee = RightLeg.addOrReplaceChild("RightKnee", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -4.0F));
		PartDefinition cube_r5 = RightKnee.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 96).mirror().addBox(-3.25F, 0.0F, 0.0F, 8.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition RightLowerKnee = RightKnee.addOrReplaceChild("RightLowerKnee", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 8.0F));
		PartDefinition cube_r6 = RightLowerKnee.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 99).mirror().addBox(-3.0F, -2.0F, -1.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 1.5F, -2.5F, 0.1745F, 0.0F, 0.0F));
		PartDefinition RightFoot = RightLowerKnee.addOrReplaceChild("RightFoot", CubeListBuilder.create().texOffs(116, 65).mirror().addBox(-3.3953F, 1.9986F, -11.0F, 8.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 8.0F, 4.0F));
		PartDefinition cube_r7 = RightFoot.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(110, 15).mirror().addBox(-2.75F, -0.25F, 0.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.1047F, 0.9986F, -6.0F, -0.0436F, 0.0F, 0.0F));
		PartDefinition RightToe1 = RightFoot.addOrReplaceChild("RightToe1", CubeListBuilder.create().texOffs(34, 109).mirror().addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(122, 91).mirror()
				.addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.6047F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition RightToe3 = RightFoot.addOrReplaceChild("RightToe3", CubeListBuilder.create().texOffs(142, 81).mirror().addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(122, 95).mirror()
				.addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4547F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition RightToe4 = RightFoot.addOrReplaceChild("RightToe4", CubeListBuilder.create().texOffs(144, 65).mirror().addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(142, 87).mirror()
				.addBox(-1.0F, 0.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.3953F, 1.9986F, -11.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition upper = main.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -1.0F));
		PartDefinition cube_r8 = upper.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, 0.0F, 18.0F, 15.0F, 14.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, -11.0F, -0.9163F, 0.0F, 0.0F));
		PartDefinition neck = upper.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, -8.0F));
		PartDefinition cube_r9 = neck.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(53, 29).addBox(-5.75F, -7.0F, -6.0F, 11.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, 2.0F, -3.0F, -0.4363F, 0.0F, 0.0F));
		PartDefinition head = neck.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 29).addBox(-6.0F, -4.0F, -13.0F, 12.0F, 8.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(66, 20).addBox(-5.5F, 4.0F, -12.5F, 11.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -3.0F, -7.0F));
		PartDefinition jaw = head.addOrReplaceChild("jaw",
				CubeListBuilder.create().texOffs(0, 81).addBox(-5.0F, 0.0F, -14.0F, 10.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(64, 0).addBox(-5.0F, -1.0F, -14.0F, 10.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(98, 28)
						.addBox(-4.5F, -8.0F, -9.0F, 9.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(154, 0).addBox(-4.5F, -7.0F, -6.0F, 9.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 4.0F, 2.0F));
		PartDefinition neckcoral = neck.addOrReplaceChild("neckcoral", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r10 = neckcoral.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(126, 117).addBox(-3.0F, -11.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, -0.1733F, 0.0133F, 0.1304F));
		PartDefinition cube_r11 = neckcoral.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(110, 131).addBox(-2.0F, -13.0F, -3.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.303F, 0.0393F, -0.1249F));
		PartDefinition cube_r12 = neckcoral.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(126, 133).addBox(-4.0F, -10.0F, -3.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, -3.0F, 4.0F, 0.1888F, -0.0504F, -0.517F));
		PartDefinition cube_r13 = neckcoral.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(122, 75).addBox(-2.75F, -11.0F, -4.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.75F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.3927F));
		PartDefinition cube_r14 = neckcoral.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(118, 99).addBox(-1.0F, -13.0F, -3.0F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3391F, 0.274F, 0.6983F));
		PartDefinition cube_r15 = neckcoral.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(34, 96).addBox(-2.0F, -10.0F, -2.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.238F, -0.2115F, -0.7332F));
		PartDefinition LeftArm = upper.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(-10.0F, -8.0F, -7.0F));
		PartDefinition cube_r16 = LeftArm.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(84, 51).mirror().addBox(-1.0F, 1.0F, -7.0F, 7.0F, 15.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, -3.0F, -3.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition LeftElbow = LeftArm.addOrReplaceChild("LeftElbow", CubeListBuilder.create().texOffs(46, 82).addBox(-3.25F, -3.0F, -2.0F, 5.0F, 29.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 11.0F, -0.75F));
		PartDefinition cube_r17 = LeftElbow.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(116, 44).addBox(-2.5F, 0.0F, 0.0F, 4.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -0.384F, 0.0F, 0.0F));
		PartDefinition LeftHand = LeftElbow.addOrReplaceChild("LeftHand", CubeListBuilder.create().texOffs(70, 117).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 26.0F, 1.0F));
		PartDefinition LeftFinger1 = LeftHand.addOrReplaceChild("LeftFinger1", CubeListBuilder.create().texOffs(138, 15).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(-3.0F, 5.0F, 2.5F, 0.0F, 1.5708F, -1.5708F));
		PartDefinition cube_r18 = LeftFinger1.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(98, 44).addBox(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, -6.0F, 0.6545F, 0.0F, 0.0F));
		PartDefinition LeftFinger2 = LeftHand.addOrReplaceChild("LeftFinger2", CubeListBuilder.create().texOffs(138, 41).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(-3.0F, 5.0F, -2.5F, 0.0F, 1.5708F, -1.5708F));
		PartDefinition cube_r19 = LeftFinger2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(138, 59).addBox(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, -6.0F, 0.6981F, 0.0F, 0.0F));
		PartDefinition LeftFinger3 = LeftHand.addOrReplaceChild("LeftFinger3", CubeListBuilder.create().texOffs(50, 138).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(3.0F, 5.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
		PartDefinition cube_r20 = LeftFinger3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(138, 109).addBox(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, -6.0F, 0.6981F, 0.0F, 0.0F));
		PartDefinition RightArm = upper.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(10.0F, -8.0F, -7.0F));
		PartDefinition cube_r21 = RightArm.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(84, 51).addBox(-6.0F, 1.0F, -7.0F, 7.0F, 15.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -3.0F, -3.0F, 0.0F, -3.1416F, 0.0F));
		PartDefinition RightElbow = RightArm.addOrReplaceChild("RightElbow", CubeListBuilder.create().texOffs(46, 82).mirror().addBox(-2.25F, -3.0F, -2.0F, 5.0F, 29.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(2.5F, 11.0F, -0.75F));
		PartDefinition cube_r22 = RightElbow.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(117, 44).mirror().addBox(-1.5F, 0.0F, 0.0F, 4.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -0.384F, 0.0F, 0.0F));
		PartDefinition RightHand = RightElbow.addOrReplaceChild("RightHand", CubeListBuilder.create().texOffs(70, 117).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 26.0F, 1.0F));
		PartDefinition RightFinger1 = RightHand.addOrReplaceChild("RightFinger1", CubeListBuilder.create().texOffs(138, 15).mirror().addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(3.0F, 5.0F, 2.5F, 0.0F, -1.5708F, 1.5708F));
		PartDefinition cube_r23 = RightFinger1.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(98, 44).mirror().addBox(-2.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 0.0F, -6.0F, 0.6545F, 0.0F, 0.0F));
		PartDefinition RightFinger2 = RightHand.addOrReplaceChild("RightFinger2", CubeListBuilder.create().texOffs(138, 41).mirror().addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(3.0F, 5.0F, -2.5F, 0.0F, -1.5708F, 1.5708F));
		PartDefinition cube_r24 = RightFinger2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(138, 59).mirror().addBox(-2.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 0.0F, -6.0F, 0.6981F, 0.0F, 0.0F));
		PartDefinition RightFinger3 = RightHand.addOrReplaceChild("RightFinger3", CubeListBuilder.create().texOffs(50, 138).mirror().addBox(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(-3.0F, 5.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));
		PartDefinition cube_r25 = RightFinger3.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(138, 109).mirror().addBox(-2.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 0.0F, -6.0F, 0.6981F, 0.0F, 0.0F));
		PartDefinition backcoral = upper.addOrReplaceChild("backcoral", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -1.0F));
		PartDefinition cube_r26 = backcoral.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 136).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, -3.0F, -1.25F, -0.0132F, -0.2217F, -0.2719F));
		PartDefinition cube_r27 = backcoral.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(16, 136).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 0.0F, 4.75F, -1.2125F, -0.7213F, 0.0092F));
		PartDefinition cube_r28 = backcoral.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 117).addBox(-2.0F, -10.0F, -2.0F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, -3.0F, -1.25F, -0.4369F, -0.1795F, 0.3057F));
		PartDefinition cube_r29 = backcoral.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(70, 131).addBox(-4.4742F, -12.5342F, -1.2756F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 4.0F, -1.25F, -2.1253F, -0.2821F, -2.9452F));
		PartDefinition cube_r30 = backcoral.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(24, 117).addBox(-2.0F, -10.0F, -2.0F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 2.75F, -0.6419F, -0.1153F, -0.0034F));
		PartDefinition cube_r31 = backcoral.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(90, 131).addBox(-1.9966F, -10.2706F, -1.9627F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, 4.0F, 1.75F, -1.496F, -0.5435F, -0.0744F));
		PartDefinition cube_r32 = backcoral.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(132, 27).addBox(-3.0F, -10.0856F, -4.3053F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -0.1704F, -0.0847F, -0.1562F));
		PartDefinition cube_r33 = backcoral.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(112, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, 5.0F, 4.0F, 0.3976F, 0.1639F, 0.0602F));
		PartDefinition cube_r34 = backcoral.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(32, 138).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 5.0F, 6.0F, 0.0583F, 0.4733F, 0.1023F));
		PartDefinition cube_r35 = backcoral.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(142, 133).addBox(-2.0F, -6.0F, -2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.0F, -2.0F, 1.0F, -0.4513F, -0.0371F, -0.8413F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgb) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
	}
}
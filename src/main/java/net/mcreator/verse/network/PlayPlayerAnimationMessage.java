package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record PlayPlayerAnimationMessage(int player, String animation, boolean override, boolean firstPerson) implements CustomPacketPayload {

	public static final Type<PlayPlayerAnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "play_player_animation"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayPlayerAnimationMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayPlayerAnimationMessage message) -> {
		buffer.writeInt(message.player);
		buffer.writeUtf(message.animation);
		buffer.writeBoolean(message.override);
		buffer.writeBoolean(message.firstPerson);
	}, (RegistryFriendlyByteBuf buffer) -> new PlayPlayerAnimationMessage(buffer.readInt(), buffer.readUtf(), buffer.readBoolean(), buffer.readBoolean()));

	@Override
	public Type<PlayPlayerAnimationMessage> type() {
		return TYPE;
	}

	public static void handleData(final PlayPlayerAnimationMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {


				// Get entity (could be player or mob)
				Entity entity = context.player().level().getEntity(message.player);
				if (entity == null) {
					VerseMod.LOGGER.warn("Tried to play animation on null entity with ID: " + message.player);
					System.out.println("[PACKET DEBUG] Entity is NULL!");
					return;
				}



				// Check if it's a LivingEntity (players and mobs)
				if (!(entity instanceof LivingEntity)) {
					VerseMod.LOGGER.warn("Tried to play animation on non-living entity: " + entity.getClass().getName());
					return;
				}

				LivingEntity livingEntity = (LivingEntity) entity;
				CompoundTag data = livingEntity.getPersistentData();



				if (message.animation.isEmpty()) {
					data.putBoolean("ResetPlayerAnimation", true);
					data.putBoolean("FirstPersonAnimation", false);
					data.remove("PlayerCurrentAnimation");
					data.remove("PlayerAnimationProgress");
					System.out.println("[PACKET DEBUG] Reset animation");
				} else {
					data.putString("PlayerCurrentAnimation", message.animation);
					data.putBoolean("OverrideCurrentAnimation", message.override);
					// Only set first person for players
					if (livingEntity instanceof Player) {
						data.putBoolean("FirstPersonAnimation", message.firstPerson);
					}

				}


			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(PlayPlayerAnimationMessage.TYPE, PlayPlayerAnimationMessage.STREAM_CODEC, PlayPlayerAnimationMessage::handleData);
	}
}
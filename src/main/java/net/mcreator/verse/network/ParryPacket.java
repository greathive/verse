package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.verse.procedures.ParrySystem;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ParryPacket() implements CustomPacketPayload {

	public static final Type<ParryPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "parry"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ParryPacket> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, ParryPacket message) -> {
				// No data to write
			},
			(RegistryFriendlyByteBuf buffer) -> new ParryPacket()
	);

	@Override
	public Type<ParryPacket> type() {
		return TYPE;
	}

	public static void handleData(final ParryPacket message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player != null) {
					// Check if player can parry
					if (!ParrySystem.canParry(player)) {
						VerseMod.LOGGER.info("Parry attempt denied - cannot parry");
						return;
					}

					// Initiate parry on server
					ParrySystem.initiateParry(player);

					// Broadcast parry animation to all nearby players
					PacketDistributor.sendToPlayersTrackingEntityAndSelf(
							player,
							new PlayPlayerAnimationMessage(
									player.getId(),
									"verse:parry_fist",
									true,
									true
							)
					);
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(ParryPacket.TYPE, ParryPacket.STREAM_CODEC, ParryPacket::handleData);
	}
}
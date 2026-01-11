package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.verse.procedures.CardClickPacketProcedure;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record CardPacketMessage(String extradata) implements CustomPacketPayload {
	public static final Type<CardPacketMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "card_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CardPacketMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CardPacketMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new CardPacketMessage(buffer.readUtf()));

	@Override
	public Type<CardPacketMessage> type() {
		return TYPE;
	}

	public static void handleData(final CardPacketMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;
				if (!world.hasChunkAt(entity.blockPosition()))
					return;

				CardClickPacketProcedure.execute(entity, inboundString);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(CardPacketMessage.TYPE, CardPacketMessage.STREAM_CODEC, CardPacketMessage::handleData);
	}
}
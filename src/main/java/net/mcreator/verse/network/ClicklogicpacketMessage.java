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

import net.mcreator.verse.procedures.ClicklogicProcedure;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record ClicklogicpacketMessage(String extradata) implements CustomPacketPayload {
	public static final Type<ClicklogicpacketMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "clicklogicpacket"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClicklogicpacketMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ClicklogicpacketMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new ClicklogicpacketMessage(buffer.readUtf()));

	@Override
	public Type<ClicklogicpacketMessage> type() {
		return TYPE;
	}

	public static void handleData(final ClicklogicpacketMessage message, final IPayloadContext context) {
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

				ClicklogicProcedure.execute(entity, inboundString);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(ClicklogicpacketMessage.TYPE, ClicklogicpacketMessage.STREAM_CODEC, ClicklogicpacketMessage::handleData);
	}
}
package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record MantraCastMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<MantraCastMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "key_mantra_cast"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MantraCastMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, MantraCastMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new MantraCastMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<MantraCastMessage> type() {
		return TYPE;
	}

	public static void handleData(final MantraCastMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(MantraCastMessage.TYPE, MantraCastMessage.STREAM_CODEC, MantraCastMessage::handleData);
	}
}
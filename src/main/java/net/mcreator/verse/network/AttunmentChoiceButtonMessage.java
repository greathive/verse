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
import net.minecraft.core.BlockPos;

import net.mcreator.verse.procedures.*;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record AttunmentChoiceButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<AttunmentChoiceButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "attunment_choice_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AttunmentChoiceButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, AttunmentChoiceButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new AttunmentChoiceButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<AttunmentChoiceButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final AttunmentChoiceButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleButtonAction(context.player(), message.buttonID, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			Attunementchoicebutton1Procedure.execute(entity);
		}
		if (buttonID == 1) {

			Attunementchoicebutton2Procedure.execute(entity);
		}
		if (buttonID == 2) {

			Attunementchoicebutton3Procedure.execute(entity);
		}
		if (buttonID == 3) {

			Attunementchoicebutton4Procedure.execute(entity);
		}
		if (buttonID == 4) {

			Attunementchoicebutton7Procedure.execute(entity);
		}
		if (buttonID == 5) {

			Attunementchoicebutton8Procedure.execute(entity);
		}
		if (buttonID == 6) {

			Attunementchoicebutton6Procedure.execute(entity);
		}
		if (buttonID == 7) {

			Attunementchoicebutton5Procedure.execute(entity);
		}
		if (buttonID == 8) {

			AttunementchoicebuttonresetProcedure.execute(entity);
		}
		if (buttonID == 9) {

			ConfirmChoiceProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(AttunmentChoiceButtonMessage.TYPE, AttunmentChoiceButtonMessage.STREAM_CODEC, AttunmentChoiceButtonMessage::handleData);
	}
}
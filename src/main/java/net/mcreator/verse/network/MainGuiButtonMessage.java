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
public record MainGuiButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<MainGuiButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "main_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MainGuiButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, MainGuiButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new MainGuiButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<MainGuiButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final MainGuiButtonMessage message, final IPayloadContext context) {
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

			OpenTalentListProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			Addpoint1Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 2) {

			Addpoint2Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 3) {

			Addpoint3Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 4) {

			Addpoint4Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 5) {

			Addpoint6Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 6) {

			Addpoint5Procedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(MainGuiButtonMessage.TYPE, MainGuiButtonMessage.STREAM_CODEC, MainGuiButtonMessage::handleData);
	}
}
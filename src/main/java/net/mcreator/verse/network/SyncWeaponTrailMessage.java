package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.verse.client.renderer.WeaponTrailRenderer;
import net.mcreator.verse.VerseMod;

import java.util.UUID;

@EventBusSubscriber
public record SyncWeaponTrailMessage(
		UUID playerId,
		String itemName,
		Vec3 spawnPos,
		Vec3 velocity,
		float yaw,
		float pitch,
		int swingCounter,
		long attackStartTime
) implements CustomPacketPayload {

	public static final Type<SyncWeaponTrailMessage> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "sync_weapon_trail")
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, SyncWeaponTrailMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, SyncWeaponTrailMessage message) -> {
				buffer.writeUUID(message.playerId);
				buffer.writeUtf(message.itemName);
				buffer.writeDouble(message.spawnPos.x);
				buffer.writeDouble(message.spawnPos.y);
				buffer.writeDouble(message.spawnPos.z);
				buffer.writeDouble(message.velocity.x);
				buffer.writeDouble(message.velocity.y);
				buffer.writeDouble(message.velocity.z);
				buffer.writeFloat(message.yaw);
				buffer.writeFloat(message.pitch);
				buffer.writeInt(message.swingCounter);
				buffer.writeLong(message.attackStartTime);
			},
			(RegistryFriendlyByteBuf buffer) -> new SyncWeaponTrailMessage(
					buffer.readUUID(),
					buffer.readUtf(),
					new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
					new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
					buffer.readFloat(),
					buffer.readFloat(),
					buffer.readInt(),
					buffer.readLong()
			)
	);

	@Override
	public Type<SyncWeaponTrailMessage> type() {
		return TYPE;
	}

	public static void handleData(final SyncWeaponTrailMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				// Store trail data for rendering
				WeaponTrailRenderer.addTrailData(
						message.playerId,
						message.itemName,
						message.spawnPos,
						message.velocity,
						message.yaw,
						message.pitch,
						message.swingCounter,
						message.attackStartTime
				);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(
				SyncWeaponTrailMessage.TYPE,
				SyncWeaponTrailMessage.STREAM_CODEC,
				SyncWeaponTrailMessage::handleData
		);
	}
}
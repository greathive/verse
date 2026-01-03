package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record CustomAttackPacket() implements CustomPacketPayload {

	public static final Type<CustomAttackPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "custom_attack"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CustomAttackPacket> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, CustomAttackPacket message) -> {
				// Empty packet - no data to write
			},
			(RegistryFriendlyByteBuf buffer) -> new CustomAttackPacket()
	);

	@Override
	public Type<CustomAttackPacket> type() {
		return TYPE;
	}

	public static void handleData(final CustomAttackPacket message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				var player = context.player();
				if (player != null) {
					var data = player.getPersistentData();

					// Get attack speed and calculate cooldown
					double attackSpeed = player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED);
					long cooldownTicks = Math.round(20.0 / attackSpeed);
					long currentTime = player.level().getGameTime();

					// Set when this attack started and when it ends
					data.putLong("AttackStartTime", currentTime);
					data.putLong("CustomAttackCooldownUntil", currentTime + cooldownTicks);

					// Clear the damage flag for the new swing
					data.remove("HasDealtDamage");
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(CustomAttackPacket.TYPE, CustomAttackPacket.STREAM_CODEC, CustomAttackPacket::handleData);
	}
}
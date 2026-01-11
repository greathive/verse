package net.mcreator.verse.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
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
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.util.SwingDataLoader;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public record CustomAttackPacket(int swing, String animName) implements CustomPacketPayload {

	public static final Type<CustomAttackPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "custom_attack"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CustomAttackPacket> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, CustomAttackPacket message) -> {
				buffer.writeInt(message.swing);
				buffer.writeUtf(message.animName);
			},
			(RegistryFriendlyByteBuf buffer) -> new CustomAttackPacket(buffer.readInt(), buffer.readUtf())
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

					// Set swing counter on server
					data.putInt("SwingCounter", message.swing);

					// Set animation on server
					data.putString("PlayerCurrentAnimation", message.animName);
					data.putBoolean("OverrideCurrentAnimation", true);
					data.putBoolean("FirstPersonAnimation", true);

					// Clear hit entities for new swing
					data.remove("HitEntitiesThisSwing");
					data.remove("LastDamageTick");

					// Broadcast animation to all nearby players (including the attacker for consistency)
					PacketDistributor.sendToPlayersTrackingEntityAndSelf(
							player,
							new PlayPlayerAnimationMessage(
									player.getId(),
									message.animName,
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
		VerseMod.addNetworkMessage(CustomAttackPacket.TYPE, CustomAttackPacket.STREAM_CODEC, CustomAttackPacket::handleData);
	}
}
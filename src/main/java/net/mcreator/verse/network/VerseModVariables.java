package net.mcreator.verse.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.mcreator.verse.VerseMod;

import java.util.function.Supplier;

@EventBusSubscriber
public class VerseModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, VerseMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		VerseMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
			player.getData(PLAYER_VARIABLES)._syncDirty = false;
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
		PlayerVariables clone = new PlayerVariables();
		clone.power = original.power;
		clone.str = original.str;
		clone.fort = original.fort;
		clone.intel = original.intel;
		clone.agility = original.agility;
		clone.attunementchooserguivariable = original.attunementchooserguivariable;
		clone.choseattunement = original.choseattunement;
		clone.Flamecharm = original.Flamecharm;
		clone.Galebreathe = original.Galebreathe;
		clone.Thundercall = original.Thundercall;
		clone.Frostdraw = original.Frostdraw;
		clone.Shadowcast = original.Shadowcast;
		clone.Ironsing = original.Ironsing;
		clone.Lifeweave = original.Lifeweave;
		clone.Bloodrend = original.Bloodrend;
		clone.expgain = original.expgain;
		clone.talentlist = original.talentlist;
		clone.validdraw = original.validdraw;
		clone.life = original.life;
		clone.echoes = original.echoes;
		clone.echoUpgrades = original.echoUpgrades;
		clone.strExp = original.strExp;
		clone.fortExp = original.fortExp;
		clone.aglExp = original.aglExp;
		clone.intExp = original.intExp;
		clone.attuneExp = original.attuneExp;
		clone.outfit = original.outfit;
		clone.outfitVisible = original.outfitVisible;
		clone.validrare = original.validrare;
		clone.hasCard = original.hasCard;
		clone.burn = original.burn;
		clone.freeze = original.freeze;
		if (!event.isWasDeath()) {
			clone.currentmantra = original.currentmantra;
			clone.PlayerIA = original.PlayerIA;
			clone.choice = original.choice;
			clone.guiOpenedTick = original.guiOpenedTick;
			clone.scroll = original.scroll;
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		boolean _syncDirty = false;
		public String currentmantra = "";
		public double power = 1.0;
		public double str = 0;
		public double fort = 0;
		public double intel = 0;
		public double agility = 0;
		public String attunementchooserguivariable = "(flamecharm)(galebreathe)(frostdraw)(thundercall)(shadowcast)(ironsing)(bloodrend)(lifeweave)";
		public boolean choseattunement = false;
		public double Flamecharm = 0;
		public double Galebreathe = 0;
		public double Thundercall = 0;
		public double Frostdraw = 0;
		public double Shadowcast = 0;
		public double Ironsing = 0;
		public double Lifeweave = 0;
		public double Bloodrend = 0;
		public double expgain = 0;
		public double PlayerIA = 0;
		public String talentlist = "\"\"";
		public String validdraw = "\"\"";
		public String life = "\"\"";
		public String choice = "\"\"";
		public double echoes = 0;
		public String echoUpgrades = "\"\"";
		public double strExp = 0;
		public double fortExp = 0;
		public double aglExp = 0;
		public double intExp = 0;
		public double attuneExp = 0;
		public String outfit = "";
		public boolean outfitVisible = true;
		public double guiOpenedTick = 0;
		public double scroll = 0;
		public String validrare = "\"\"";
		public boolean hasCard = false;
		public String burn = "\"\"";
		public String freeze = "\"\"";

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putString("currentmantra", currentmantra);
			nbt.putDouble("power", power);
			nbt.putDouble("str", str);
			nbt.putDouble("fort", fort);
			nbt.putDouble("intel", intel);
			nbt.putDouble("agility", agility);
			nbt.putString("attunementchooserguivariable", attunementchooserguivariable);
			nbt.putBoolean("choseattunement", choseattunement);
			nbt.putDouble("Flamecharm", Flamecharm);
			nbt.putDouble("Galebreathe", Galebreathe);
			nbt.putDouble("Thundercall", Thundercall);
			nbt.putDouble("Frostdraw", Frostdraw);
			nbt.putDouble("Shadowcast", Shadowcast);
			nbt.putDouble("Ironsing", Ironsing);
			nbt.putDouble("Lifeweave", Lifeweave);
			nbt.putDouble("Bloodrend", Bloodrend);
			nbt.putDouble("expgain", expgain);
			nbt.putDouble("PlayerIA", PlayerIA);
			nbt.putString("talentlist", talentlist);
			nbt.putString("validdraw", validdraw);
			nbt.putString("life", life);
			nbt.putString("choice", choice);
			nbt.putDouble("echoes", echoes);
			nbt.putString("echoUpgrades", echoUpgrades);
			nbt.putDouble("strExp", strExp);
			nbt.putDouble("fortExp", fortExp);
			nbt.putDouble("aglExp", aglExp);
			nbt.putDouble("intExp", intExp);
			nbt.putDouble("attuneExp", attuneExp);
			nbt.putString("outfit", outfit);
			nbt.putBoolean("outfitVisible", outfitVisible);
			nbt.putDouble("guiOpenedTick", guiOpenedTick);
			nbt.putDouble("scroll", scroll);
			nbt.putString("validrare", validrare);
			nbt.putBoolean("hasCard", hasCard);
			nbt.putString("burn", burn);
			nbt.putString("freeze", freeze);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			currentmantra = nbt.getString("currentmantra");
			power = nbt.getDouble("power");
			str = nbt.getDouble("str");
			fort = nbt.getDouble("fort");
			intel = nbt.getDouble("intel");
			agility = nbt.getDouble("agility");
			attunementchooserguivariable = nbt.getString("attunementchooserguivariable");
			choseattunement = nbt.getBoolean("choseattunement");
			Flamecharm = nbt.getDouble("Flamecharm");
			Galebreathe = nbt.getDouble("Galebreathe");
			Thundercall = nbt.getDouble("Thundercall");
			Frostdraw = nbt.getDouble("Frostdraw");
			Shadowcast = nbt.getDouble("Shadowcast");
			Ironsing = nbt.getDouble("Ironsing");
			Lifeweave = nbt.getDouble("Lifeweave");
			Bloodrend = nbt.getDouble("Bloodrend");
			expgain = nbt.getDouble("expgain");
			PlayerIA = nbt.getDouble("PlayerIA");
			talentlist = nbt.getString("talentlist");
			validdraw = nbt.getString("validdraw");
			life = nbt.getString("life");
			choice = nbt.getString("choice");
			echoes = nbt.getDouble("echoes");
			echoUpgrades = nbt.getString("echoUpgrades");
			strExp = nbt.getDouble("strExp");
			fortExp = nbt.getDouble("fortExp");
			aglExp = nbt.getDouble("aglExp");
			intExp = nbt.getDouble("intExp");
			attuneExp = nbt.getDouble("attuneExp");
			outfit = nbt.getString("outfit");
			outfitVisible = nbt.getBoolean("outfitVisible");
			guiOpenedTick = nbt.getDouble("guiOpenedTick");
			scroll = nbt.getDouble("scroll");
			validrare = nbt.getString("validrare");
			hasCard = nbt.getBoolean("hasCard");
			burn = nbt.getString("burn");
			freeze = nbt.getString("freeze");
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VerseMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			buffer.writeInt(message.player());
			buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt());
			message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
			return message;
		});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					Entity player = context.player().level().getEntity(message.player);
					if (player == null)
						return;
					player.getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()));
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}
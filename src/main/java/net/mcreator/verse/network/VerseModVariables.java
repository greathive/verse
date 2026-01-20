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
		clone.validdraw = original.validdraw;
		clone.talentlist = original.talentlist;
		clone.mantrastorage = original.mantrastorage;
		clone.mantrahotbar = original.mantrahotbar;
		clone.life = original.life;
		clone.freeze = original.freeze;
		clone.echoUpgrades = original.echoUpgrades;
		clone.burn = original.burn;
		clone.attunementchooserguivariable = original.attunementchooserguivariable;
		clone.Thundercall = original.Thundercall;
		clone.strExp = original.strExp;
		clone.str = original.str;
		clone.Shadowcast = original.Shadowcast;
		clone.power = original.power;
		clone.Lifeweave = original.Lifeweave;
		clone.Ironsing = original.Ironsing;
		clone.intExp = original.intExp;
		clone.intel = original.intel;
		clone.Galebreathe = original.Galebreathe;
		clone.Frostdraw = original.Frostdraw;
		clone.fortExp = original.fortExp;
		clone.fort = original.fort;
		clone.fold = original.fold;
		clone.Flamecharm = original.Flamecharm;
		clone.expgain = original.expgain;
		clone.echoes = original.echoes;
		clone.Bloodrend = original.Bloodrend;
		clone.attuneExp = original.attuneExp;
		clone.aglExp = original.aglExp;
		clone.agility = original.agility;
		clone.hasCard = original.hasCard;
		clone.choseattunement = original.choseattunement;
		if (!event.isWasDeath()) {
			clone.choice = original.choice;
			clone.ace = original.ace;
			clone.secondaryguitick = original.secondaryguitick;
			clone.scroll = original.scroll;
			clone.guiOpenedTick = original.guiOpenedTick;
			clone.pickedcards = original.pickedcards;
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		boolean _syncDirty = false;
		public String validdraw = "\"\"";
		public String talentlist = "\"\"";
		public String mantrastorage = "";
		public String mantrahotbar = "";
		public String life = "\"\"";
		public String freeze = "\"\"";
		public String echoUpgrades = "\"\"";
		public String choice = "\"\"";
		public String burn = "\"\"";
		public String attunementchooserguivariable = "(flamecharm)(galebreathe)(frostdraw)(thundercall)(shadowcast)(ironsing)(bloodrend)(lifeweave)";
		public String ace = "norm";
		public double Thundercall = 0;
		public double strExp = 0;
		public double str = 0;
		public double Shadowcast = 0;
		public double secondaryguitick = 0;
		public double scroll = 0;
		public double power = 1.0;
		public double Lifeweave = 0;
		public double Ironsing = 0;
		public double intExp = 0;
		public double intel = 0;
		public double guiOpenedTick = 0;
		public double Galebreathe = 0;
		public double Frostdraw = 0;
		public double fortExp = 0;
		public double fort = 0;
		public double fold = 0;
		public double Flamecharm = 0;
		public double expgain = 0;
		public double echoes = 0;
		public double Bloodrend = 0;
		public double attuneExp = 0;
		public double aglExp = 0;
		public double agility = 0;
		public boolean pickedcards = false;
		public boolean hasCard = false;
		public boolean choseattunement = false;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putString("validdraw", validdraw);
			nbt.putString("talentlist", talentlist);
			nbt.putString("mantrastorage", mantrastorage);
			nbt.putString("mantrahotbar", mantrahotbar);
			nbt.putString("life", life);
			nbt.putString("freeze", freeze);
			nbt.putString("echoUpgrades", echoUpgrades);
			nbt.putString("choice", choice);
			nbt.putString("burn", burn);
			nbt.putString("attunementchooserguivariable", attunementchooserguivariable);
			nbt.putString("ace", ace);
			nbt.putDouble("Thundercall", Thundercall);
			nbt.putDouble("strExp", strExp);
			nbt.putDouble("str", str);
			nbt.putDouble("Shadowcast", Shadowcast);
			nbt.putDouble("secondaryguitick", secondaryguitick);
			nbt.putDouble("scroll", scroll);
			nbt.putDouble("power", power);
			nbt.putDouble("Lifeweave", Lifeweave);
			nbt.putDouble("Ironsing", Ironsing);
			nbt.putDouble("intExp", intExp);
			nbt.putDouble("intel", intel);
			nbt.putDouble("guiOpenedTick", guiOpenedTick);
			nbt.putDouble("Galebreathe", Galebreathe);
			nbt.putDouble("Frostdraw", Frostdraw);
			nbt.putDouble("fortExp", fortExp);
			nbt.putDouble("fort", fort);
			nbt.putDouble("fold", fold);
			nbt.putDouble("Flamecharm", Flamecharm);
			nbt.putDouble("expgain", expgain);
			nbt.putDouble("echoes", echoes);
			nbt.putDouble("Bloodrend", Bloodrend);
			nbt.putDouble("attuneExp", attuneExp);
			nbt.putDouble("aglExp", aglExp);
			nbt.putDouble("agility", agility);
			nbt.putBoolean("pickedcards", pickedcards);
			nbt.putBoolean("hasCard", hasCard);
			nbt.putBoolean("choseattunement", choseattunement);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			validdraw = nbt.getString("validdraw");
			talentlist = nbt.getString("talentlist");
			mantrastorage = nbt.getString("mantrastorage");
			mantrahotbar = nbt.getString("mantrahotbar");
			life = nbt.getString("life");
			freeze = nbt.getString("freeze");
			echoUpgrades = nbt.getString("echoUpgrades");
			choice = nbt.getString("choice");
			burn = nbt.getString("burn");
			attunementchooserguivariable = nbt.getString("attunementchooserguivariable");
			ace = nbt.getString("ace");
			Thundercall = nbt.getDouble("Thundercall");
			strExp = nbt.getDouble("strExp");
			str = nbt.getDouble("str");
			Shadowcast = nbt.getDouble("Shadowcast");
			secondaryguitick = nbt.getDouble("secondaryguitick");
			scroll = nbt.getDouble("scroll");
			power = nbt.getDouble("power");
			Lifeweave = nbt.getDouble("Lifeweave");
			Ironsing = nbt.getDouble("Ironsing");
			intExp = nbt.getDouble("intExp");
			intel = nbt.getDouble("intel");
			guiOpenedTick = nbt.getDouble("guiOpenedTick");
			Galebreathe = nbt.getDouble("Galebreathe");
			Frostdraw = nbt.getDouble("Frostdraw");
			fortExp = nbt.getDouble("fortExp");
			fort = nbt.getDouble("fort");
			fold = nbt.getDouble("fold");
			Flamecharm = nbt.getDouble("Flamecharm");
			expgain = nbt.getDouble("expgain");
			echoes = nbt.getDouble("echoes");
			Bloodrend = nbt.getDouble("Bloodrend");
			attuneExp = nbt.getDouble("attuneExp");
			aglExp = nbt.getDouble("aglExp");
			agility = nbt.getDouble("agility");
			pickedcards = nbt.getBoolean("pickedcards");
			hasCard = nbt.getBoolean("hasCard");
			choseattunement = nbt.getBoolean("choseattunement");
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
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.world.inventory.TalentDisplayMenu;
import net.mcreator.verse.world.inventory.OutfitCraftingMenu;
import net.mcreator.verse.world.inventory.MainGuiMenu;
import net.mcreator.verse.world.inventory.CardHandMenu;
import net.mcreator.verse.world.inventory.AttunmentChoiceMenu;
import net.mcreator.verse.network.MenuStateUpdateMessage;
import net.mcreator.verse.VerseMod;

import java.util.Map;

public class VerseModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, VerseMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<MainGuiMenu>> MAIN_GUI = REGISTRY.register("main_gui", () -> IMenuTypeExtension.create(MainGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<AttunmentChoiceMenu>> ATTUNMENT_CHOICE = REGISTRY.register("attunment_choice", () -> IMenuTypeExtension.create(AttunmentChoiceMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<OutfitCraftingMenu>> OUTFIT_CRAFTING = REGISTRY.register("outfit_crafting", () -> IMenuTypeExtension.create(OutfitCraftingMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<TalentDisplayMenu>> TALENT_DISPLAY = REGISTRY.register("talent_display", () -> IMenuTypeExtension.create(TalentDisplayMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CardHandMenu>> CARD_HAND = REGISTRY.register("card_hand", () -> IMenuTypeExtension.create(CardHandMenu::new));

	public interface MenuAccessor {
		Map<String, Object> getMenuState();

		Map<Integer, Slot> getSlots();

		default void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
			getMenuState().put(elementType + ":" + name, elementState);
			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new MenuStateUpdateMessage(elementType, name, elementState));
			} else if (player.level().isClientSide) {
				if (Minecraft.getInstance().screen instanceof VerseModScreens.ScreenAccessor accessor && needClientUpdate)
					accessor.updateMenuState(elementType, name, elementState);
				PacketDistributor.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
			}
		}

		default <T> T getMenuState(int elementType, String name, T defaultValue) {
			try {
				return (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
			} catch (ClassCastException e) {
				return defaultValue;
			}
		}
	}
}
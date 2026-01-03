/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.verse.client.gui.TalentDisplayScreen;
import net.mcreator.verse.client.gui.OutfitCraftingScreen;
import net.mcreator.verse.client.gui.MainGuiScreen;
import net.mcreator.verse.client.gui.CardHandScreen;
import net.mcreator.verse.client.gui.AttunmentChoiceScreen;

@EventBusSubscriber(Dist.CLIENT)
public class VerseModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(VerseModMenus.MAIN_GUI.get(), MainGuiScreen::new);
		event.register(VerseModMenus.ATTUNMENT_CHOICE.get(), AttunmentChoiceScreen::new);
		event.register(VerseModMenus.OUTFIT_CRAFTING.get(), OutfitCraftingScreen::new);
		event.register(VerseModMenus.TALENT_DISPLAY.get(), TalentDisplayScreen::new);
		event.register(VerseModMenus.CARD_HAND.get(), CardHandScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}
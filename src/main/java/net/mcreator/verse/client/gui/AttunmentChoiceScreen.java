package net.mcreator.verse.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.verse.world.inventory.AttunmentChoiceMenu;
import net.mcreator.verse.procedures.ReturnchoiceProcedure;
import net.mcreator.verse.network.AttunmentChoiceButtonMessage;
import net.mcreator.verse.init.VerseModScreens;

import com.mojang.blaze3d.systems.RenderSystem;

public class AttunmentChoiceScreen extends AbstractContainerScreen<AttunmentChoiceMenu> implements VerseModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_emptybutton;
	private ImageButton imagebutton_emptybutton1;
	private ImageButton imagebutton_2;
	private ImageButton imagebutton_emptybutton2;
	private ImageButton imagebutton_emptybutton3;
	private ImageButton imagebutton_emptybutton4;
	private ImageButton imagebutton_emptybutton5;
	private ImageButton imagebutton_emptybutton6;
	private ImageButton imagebutton_emptybutton7;
	private ImageButton imagebutton_emptybutton8;

	public AttunmentChoiceScreen(AttunmentChoiceMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 150;
		this.imageHeight = 200;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(ResourceLocation.parse("verse:textures/screens/newtextbox.png"), this.leftPos + -124, this.topPos + 54, 0, 0, 218, 84, 218, 84);
		guiGraphics.blit(ResourceLocation.parse("verse:textures/screens/thinbox.png"), this.leftPos + -65, this.topPos + 49, 0, 0, 94, 22, 94, 22);
		guiGraphics.blit(ResourceLocation.parse("verse:textures/screens/thinbox.png"), this.leftPos + -65, this.topPos + 121, 0, 0, 94, 22, 94, 22);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.verse.attunment_choice.label_this_choice_is_permanent"), -118, 70, -1, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.verse.attunment_choice.label_permanent"), -118, 80, -1, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.verse.attunment_choice.label_through_gameplay_however_they"), -118, 90, -1, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.verse.attunment_choice.label_until_you_experience_permadeath"), -55, 101, -1, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.verse.attunment_choice.label_attunements"), -47, 55, -1, false);
		guiGraphics.drawString(this.font, ReturnchoiceProcedure.execute(entity), -54, 127, -1, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_emptybutton = new ImageButton(this.leftPos + -125, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(0, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton);
		imagebutton_emptybutton1 = new ImageButton(this.leftPos + -65, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(1, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton1);
		imagebutton_2 = new ImageButton(this.leftPos + -5, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")), e -> {
			int x = AttunmentChoiceScreen.this.x;
			int y = AttunmentChoiceScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(2, x, y, z));
				AttunmentChoiceButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_2);
		imagebutton_emptybutton2 = new ImageButton(this.leftPos + 55, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(3, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton2);
		imagebutton_emptybutton3 = new ImageButton(this.leftPos + -5, this.topPos + 0, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(4, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton3);
		imagebutton_emptybutton4 = new ImageButton(this.leftPos + 55, this.topPos + 0, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(5, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 5, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton4);
		imagebutton_emptybutton5 = new ImageButton(this.leftPos + -65, this.topPos + 0, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(6, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 6, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton5);
		imagebutton_emptybutton6 = new ImageButton(this.leftPos + -125, this.topPos + 0, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(7, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 7, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton6);
		imagebutton_emptybutton7 = new ImageButton(this.leftPos + 100, this.topPos + 75, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(8, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 8, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton7);
		imagebutton_emptybutton8 = new ImageButton(this.leftPos + 110, this.topPos + 0, 32, 32,
				new WidgetSprites(ResourceLocation.parse("verse:textures/screens/checkbutton.png"), ResourceLocation.parse("verse:textures/screens/outlinecheckbutton.png")), e -> {
					int x = AttunmentChoiceScreen.this.x;
					int y = AttunmentChoiceScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new AttunmentChoiceButtonMessage(9, x, y, z));
						AttunmentChoiceButtonMessage.handleButtonAction(entity, 9, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton8);
	}
}
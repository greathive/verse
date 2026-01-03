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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.verse.world.inventory.MainGuiMenu;
import net.mcreator.verse.procedures.AttunementcountProcedure;
import net.mcreator.verse.procedures.Attunementcount4Procedure;
import net.mcreator.verse.procedures.Attunementcount3Procedure;
import net.mcreator.verse.procedures.Attunementcount2Procedure;
import net.mcreator.verse.network.MainGuiButtonMessage;
import net.mcreator.verse.init.VerseModScreens;

import com.mojang.blaze3d.systems.RenderSystem;

public class MainGuiScreen extends AbstractContainerScreen<MainGuiMenu> implements VerseModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_t;
	private ImageButton imagebutton_emptybutton;
	private ImageButton imagebutton_emptybutton1;
	private ImageButton imagebutton_2;
	private ImageButton imagebutton_emptybutton2;
	private ImageButton imagebutton_emptybutton3;
	private ImageButton imagebutton_emptybutton4;
	private ImageButton imagebutton_nobg;
	private ImageButton imagebutton_nobg1;

	public MainGuiScreen(MainGuiMenu container, Inventory inventory, Component text) {
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
	}

	@Override
	public void init() {
		super.init();
		button_t = Button.builder(Component.translatable("gui.verse.main_gui.button_t"), e -> {
			int x = MainGuiScreen.this.x;
			int y = MainGuiScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new MainGuiButtonMessage(0, x, y, z));
				MainGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 43, this.topPos + 91, 30, 20).build();
		this.addRenderableWidget(button_t);
		imagebutton_emptybutton = new ImageButton(this.leftPos + -125, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = MainGuiScreen.this.x;
					int y = MainGuiScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new MainGuiButtonMessage(1, x, y, z));
						MainGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
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
					int x = MainGuiScreen.this.x;
					int y = MainGuiScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new MainGuiButtonMessage(2, x, y, z));
						MainGuiButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton1);
		imagebutton_2 = new ImageButton(this.leftPos + -5, this.topPos + 156, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")), e -> {
			int x = MainGuiScreen.this.x;
			int y = MainGuiScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new MainGuiButtonMessage(3, x, y, z));
				MainGuiButtonMessage.handleButtonAction(entity, 3, x, y, z);
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
					int x = MainGuiScreen.this.x;
					int y = MainGuiScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new MainGuiButtonMessage(4, x, y, z));
						MainGuiButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton2);
		imagebutton_emptybutton3 = new ImageButton(this.leftPos + -6, this.topPos + -22, 42, 42, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/nobg.png"), ResourceLocation.parse("verse:textures/screens/nobg.png")), e -> {
			int x = MainGuiScreen.this.x;
			int y = MainGuiScreen.this.y;
			if (Attunementcount2Procedure.execute(entity)) {
				PacketDistributor.sendToServer(new MainGuiButtonMessage(5, x, y, z));
				MainGuiButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = MainGuiScreen.this.x;
				int y = MainGuiScreen.this.y;
				if (Attunementcount2Procedure.execute(entity))
					guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton3);
		imagebutton_emptybutton4 = new ImageButton(this.leftPos + 55, this.topPos + -22, 40, 40, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/emptybutton.png"), ResourceLocation.parse("verse:textures/screens/emptybutton.png")),
				e -> {
					int x = MainGuiScreen.this.x;
					int y = MainGuiScreen.this.y;
					if (AttunementcountProcedure.execute(entity)) {
						PacketDistributor.sendToServer(new MainGuiButtonMessage(6, x, y, z));
						MainGuiButtonMessage.handleButtonAction(entity, 6, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = MainGuiScreen.this.x;
				int y = MainGuiScreen.this.y;
				if (AttunementcountProcedure.execute(entity))
					guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_emptybutton4);
		imagebutton_nobg = new ImageButton(this.leftPos + -66, this.topPos + 5, 42, 42, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/nobg.png"), ResourceLocation.parse("verse:textures/screens/nobg.png")), e -> {
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = MainGuiScreen.this.x;
				int y = MainGuiScreen.this.y;
				if (Attunementcount3Procedure.execute(entity))
					guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_nobg);
		imagebutton_nobg1 = new ImageButton(this.leftPos + -126, this.topPos + 5, 42, 42, new WidgetSprites(ResourceLocation.parse("verse:textures/screens/nobg.png"), ResourceLocation.parse("verse:textures/screens/nobg.png")), e -> {
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = MainGuiScreen.this.x;
				int y = MainGuiScreen.this.y;
				if (Attunementcount4Procedure.execute(entity))
					guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_nobg1);
	}
}
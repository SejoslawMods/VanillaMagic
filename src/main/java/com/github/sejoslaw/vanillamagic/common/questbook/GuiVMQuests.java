package com.github.sejoslaw.vanillamagic.common.questbook;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CClientStatusPacket;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

/**
 * GUI which shows Quests progress. Remake of the old Achievements GUI.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class GuiVMQuests extends Screen { //implements IProgressMeter {
//	/**
//	 * Is the smallest column used to display a Quest on the GUI.
//	 */
//	public static int MIN_DISPLAY_COLUMN;
//	/**
//	 * Is the smallest row used to display a Quest on the GUI.
//	 */
//	public static int MIN_DISPLAY_ROW;
//	/**
//	 * Is the biggest column used to display a Quest on the GUI.
//	 */
//	public static int MAX_DISPLAY_COLUMN;
//	/**
//	 * Is the biggest row used to display a Quest on the GUI.
//	 */
//	public static int MAX_DISPLAY_ROW;
//
//	// Quest columns and rows
//	private static final int X_MIN = MIN_DISPLAY_COLUMN * 24 - 112;
//	private static final int Y_MIN = MIN_DISPLAY_ROW * 24 - 112;
//	private static final int X_MAX = MAX_DISPLAY_COLUMN * 24 - 77;
//	private static final int Y_MAX = MAX_DISPLAY_ROW * 24 - 77;
//
//	// Quest multipliers for window bounds
//	private static final int X_MIN_WIDTH_MULTIPLIER = 5;
//	private static final int Y_MIN_HEIGHT_MULTIPLIER = 2;
//	private static final int X_MAX_WIDTH_MULTIPLIER = 100;
//	private static final int Y_MAX_HEIGHT_MULTIPLIER = 8;
//
//	// Needed pixels for Y max
//	private static final int Y_MAX_HEIGHT_NEEDED_PIXELS = 500;
//
	/**
	 * Parent screen. (This should be in-game screen).
	 */
	private Screen parentScreen;
	/**
	 * Player who opened book.
	 */
	private PlayerEntity player;
	/**
	 * Is Player scrolling the screen
	 */
	private boolean isScrolling;
//	/**
//	 * Are the Quests loading
//	 */
//	private boolean loadingQuests = true;

	protected int imageWidth = 256;
	protected int imageHeight = 202;
//	protected double xLastScroll;
//	protected double yLastScroll;
//	protected float zoom = 1.0F;
//	protected double xScrollO;
//	protected double yScrollO;
//	protected double xScrollP;
//	protected double yScrollP;
//	protected double xScrollTarget;
//	protected double yScrollTarget;
//
	public GuiVMQuests() {
		super(new TranslationTextComponent("questbook.title"));

//		this.xScrollTarget = (double) (QuestList.get(0).getPosition().getX() * 24 - 70 - 12);
//		this.xScrollO = this.xScrollTarget;
//		this.xScrollP = this.xScrollTarget;
//
//		this.yScrollTarget = (double) (QuestList.get(0).getPosition().getY() * 24 - 70);
//		this.yScrollO = this.yScrollTarget;
//		this.yScrollP = this.yScrollTarget;
	}

	public GuiVMQuests setParentScreen(Screen screen) {
		this.parentScreen = screen;
		return this;
	}

	public GuiVMQuests setOpener(PlayerEntity player) {
		this.player = player;
		return this;
	}

//	public void onStatsUpdated() {
//		if (this.loadingQuests) {
//			this.loadingQuests = false;
//		}
//	}

	protected void init() //{
		this.minecraft.getConnection().sendPacket(new CClientStatusPacket(CClientStatusPacket.State.REQUEST_STATS));

		this.buttons.clear();
		this.buttons.add(new Button(80, 20, 80, 20, I18n.format("gui.done", new Object[0]),
				b -> Minecraft.getInstance().displayGuiScreen(this.parentScreen)));
	}

	public void render(int mousePosX, int mousePosY, float ticks) {
		this.renderBackground();
		this.drawQuestsScreen(mousePosX, mousePosY, ticks);
		this.drawCenteredString(this.font, "Quests", this.width / 2, 5, Color.WHITE.getRGB());
		super.render(mousePosX, mousePosY, ticks);
	}

	public boolean mouseDragged(double lastMousePosX, double lastMousePoxY, int p_mouseDragged_5_, double mpusePosX, double mousePosY) {
		if (p_mouseDragged_5_ != 0) {
			this.isScrolling = false;
			return false;
		} else {
			if (!this.isScrolling) {
				this.isScrolling = true;
			}
			return true;
		}
	}

	private void drawQuestsScreen(int mousePosX, int mousePosY, float ticks) {
		int centerWidth = (this.width - this.imageWidth) / 2;
		int centerHeight = (this.height - this.imageHeight) / 2;

		int centerWidthWithOffset = centerWidth + 16;
		int centerHeightWithOffset = centerHeight + 17;
	}

//	public void drawQuestsScreen(int mouseX, int mouseY, float partialTicks) {
//		int xScroll = MathHelper.floor(this.xScrollO + (this.xScrollP - this.xScrollO) * (double) partialTicks);
//		int yScroll = MathHelper.floor(this.yScrollO + (this.yScrollP - this.yScrollO) * (double) partialTicks);
//
//		// Scrolling
//		if (xScroll < X_MIN * X_MIN_WIDTH_MULTIPLIER) {
//			xScroll = X_MIN * X_MIN_WIDTH_MULTIPLIER;
//		}
//
//		if (yScroll < Y_MIN * Y_MIN_HEIGHT_MULTIPLIER) {
//			yScroll = Y_MIN * Y_MIN_HEIGHT_MULTIPLIER;
//		}
//
//		if (xScroll >= X_MAX / X_MAX_WIDTH_MULTIPLIER) {
//			xScroll = (X_MAX - 1) / X_MAX_WIDTH_MULTIPLIER;
//		}
//
//		if (yScroll >= Y_MAX_HEIGHT_NEEDED_PIXELS + Y_MAX / Y_MAX_HEIGHT_MULTIPLIER) {
//			yScroll = Y_MAX_HEIGHT_NEEDED_PIXELS + (Y_MAX - 1) / Y_MAX_HEIGHT_MULTIPLIER;
//		}
//
//		int centerWidth = (this.width - this.imageWidth) / 2;
//		int centerHeight = (this.height - this.imageHeight) / 2;
//
//		int centerWidthWithOffset = centerWidth + 16;
//		int centerHeightWithOffset = centerHeight + 17;
//
//		this.zLevel = 0.0F;
//
//		GlStateManager.depthFunc(518);
//		GlStateManager.pushMatrix();
//		GlStateManager.translate((float) centerWidthWithOffset, (float) centerHeightWithOffset, -200.0F);
//		GlStateManager.scale(1.0F / this.zoom, 1.0F / this.zoom, 1.0F);
//		GlStateManager.enableTexture2D();
//		GlStateManager.disableLighting();
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.enableColorMaterial();
//
//		int xScrollMoved = xScroll + 288 >> 4;
//		int yScrollMoved = yScroll + 288 >> 4;
//
//		int drawBackgroundPosXOffset = (xScroll + 288) % 16;
//		int drawBackgroundPosYOffset = (yScroll + 288) % 16;
//
//		Random random = new Random();
//		float localZoom = 16.0F / this.zoom;
//
//		// Draw Background
//		for (int drawBackgroundPosY = 0; (float) drawBackgroundPosY * localZoom
//				- (float) drawBackgroundPosYOffset < 155.0F; ++drawBackgroundPosY) {
//			float color = 0.6F - (float) (yScrollMoved + drawBackgroundPosY) / 25.0F * 0.3F;
//			GlStateManager.color(color, color, color, 1.0F);
//
//			for (int drawBackgroundPosX = 0; (float) drawBackgroundPosX * localZoom
//					- (float) drawBackgroundPosXOffset < 224.0F; ++drawBackgroundPosX) {
//				random.setSeed((long) (this.mc.getSession().getPlayerID().hashCode() + xScrollMoved + drawBackgroundPosX
//						+ (yScrollMoved + drawBackgroundPosY) * 16));
//				int backgroundHeight = random.nextInt(1 + yScrollMoved + drawBackgroundPosY)
//						+ (yScrollMoved + drawBackgroundPosY) / 2;
//				TextureAtlasSprite texture = this.getTexture(Blocks.SAND);
//
//				if (backgroundHeight <= 37 && yScrollMoved + drawBackgroundPosY != 35) {
//					if (backgroundHeight == 22) {
//						if (random.nextInt(2) == 0) {
//							texture = this.getTexture(Blocks.DIAMOND_ORE);
//						} else {
//							texture = this.getTexture(Blocks.REDSTONE_ORE);
//						}
//					} else if (backgroundHeight == 10) {
//						texture = this.getTexture(Blocks.IRON_ORE);
//					} else if (backgroundHeight == 8) {
//						texture = this.getTexture(Blocks.COAL_ORE);
//					} else if (backgroundHeight > 4) {
//						texture = this.getTexture(Blocks.STONE);
//					} else if (backgroundHeight > 0) {
//						texture = this.getTexture(Blocks.DIRT);
//					}
//				} else {
//					Block block = Blocks.BEDROCK;
//					texture = this.getTexture(block);
//				}
//
//				this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//				// Main background drawing method
//				this.drawTexturedModalRect(drawBackgroundPosX * 16 - drawBackgroundPosXOffset,
//						drawBackgroundPosY * 16 - drawBackgroundPosYOffset, texture, 16, 16);
//			}
//		}
//
//		GlStateManager.enableDepth();
//		GlStateManager.depthFunc(515);
//
//		List<IQuest> questList = QuestList.getQuests();
//
//		for (int i = 0; i < questList.size(); ++i) { // j5 -> i
//			IQuest quest = questList.get(i);
//
//			if (quest.getParent() != null && questList.contains(quest.getParent())) {
//				int column = quest.getPosition().getX() * 24 - xScroll + 11; // displayColumn
//				int row = quest.getPosition().getY() * 24 - yScroll + 11; // displayRow
//
//				int parentColumn = quest.getParent().getPosition().getX() * 24 - xScroll + 11;
//				int parentRow = quest.getParent().getPosition().getY() * 24 - yScroll + 11;
//
//				boolean hasQuestUnlocked = QuestUtil.hasQuestUnlocked(this.player, quest); // this._statsManager.hasAchievementUnlocked(quest);
//				boolean canUnlockQuest = QuestUtil.canUnlockQuest(this.player, quest); // this.statFileWriter.canUnlockAchievement(quest);
//
//				int requirementCount = QuestUtil.countRequirementsUntilAvailable(this.player, quest);
//
//				if (requirementCount <= 4) {
//					int color = -16777216;
//
//					if (hasQuestUnlocked) {
//						color = -6250336;
//					} else if (canUnlockQuest) {
//						color = -16711936;
//					}
//
//					this.drawHorizontalLine(column, parentColumn, row, color);
//					this.drawVerticalLine(parentColumn, row, parentRow, color);
//
//					if (column > parentColumn) {
//						this.drawTexturedModalRect(column - 11 - 7, row - 5, 114, 234, 7, 11);
//					} else if (column < parentColumn) {
//						this.drawTexturedModalRect(column + 11, row - 5, 107, 234, 7, 11);
//					} else if (row > parentRow) {
//						this.drawTexturedModalRect(column - 5, row - 11 - 7, 96, 234, 11, 7);
//					} else if (row < parentRow) {
//						this.drawTexturedModalRect(column - 5, row + 11, 96, 241, 11, 7);
//					}
//				}
//			}
//		}
//
//		IQuest quest = null;
//
//		float zoomX = (float) (mouseX - centerWidthWithOffset) * this.zoom;
//		float zoomY = (float) (mouseY - centerHeightWithOffset) * this.zoom;
//
//		RenderHelper.enableGUIStandardItemLighting();
//		GlStateManager.disableLighting();
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.enableColorMaterial();
//
//		for (int i = 0; i < questList.size(); ++i) {
//			IQuest quest2 = questList.get(i);
//
//			int iconPosX = quest2.getPosition().getX() * 24 - xScroll;
//			int iconPosY = quest2.getPosition().getY() * 24 - yScroll;
//
//			if (iconPosX >= -24 && iconPosY >= -24 && (float) iconPosX <= 224.0F * this.zoom
//					&& (float) iconPosY <= 155.0F * this.zoom) {
//				int requirementCount = QuestUtil.countRequirementsUntilAvailable(this.player, quest2);
//
//				if (QuestUtil.hasQuestUnlocked(this.player, quest2)) {
//					GlStateManager.color(0.75F, 0.75F, 0.75F, 1.0F);
//				} else if (QuestUtil.canUnlockQuest(this.player, quest2)) {
//					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//				} else if (requirementCount < 3) {
//					GlStateManager.color(0.3F, 0.3F, 0.3F, 1.0F);
//				} else if (requirementCount == 3) {
//					GlStateManager.color(0.2F, 0.2F, 0.2F, 1.0F);
//				} else {
//					if (requirementCount != 4) {
//						continue;
//					}
//
//					GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
//				}
//
//				GlStateManager.enableBlend();
//				GlStateManager.disableBlend();
//
//				if (!QuestUtil.canUnlockQuest(this.player, quest2)) {
//					GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
//				}
//
//				GlStateManager.disableLighting();
//				GlStateManager.enableCull();
//
//				this.itemRender.renderItemAndEffectIntoGUI(quest2.getIcon(), iconPosX + 3, iconPosY + 3);
//
//				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
//						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//				GlStateManager.disableLighting();
//				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//
//				if (zoomX >= (float) iconPosX && zoomX <= (float) (iconPosX + 22) && zoomY >= (float) iconPosY
//						&& zoomY <= (float) (iconPosY + 22)) {
//					quest = quest2;
//				}
//			}
//		}
//
//		GlStateManager.disableDepth();
//		GlStateManager.enableBlend();
//		GlStateManager.popMatrix();
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//
//		this.zLevel = 0.0F;
//
//		GlStateManager.depthFunc(515);
//		GlStateManager.disableDepth();
//		GlStateManager.enableTexture2D();
//
//		super.drawScreen(mouseX, mouseY, partialTicks);
//
//		if (quest != null) {
//			String unformattedQuestName = quest.getQuestData().getStatName().getUnformattedText();
//			String questDescription = quest.getQuestDescription();
//
//			int mouseXOffset = mouseX + 12;
//			int mouseYOffset = mouseY - 4;
//
//			int requirementCount = QuestUtil.countRequirementsUntilAvailable(this.player, quest);
//
//			if (QuestUtil.canUnlockQuest(this.player, quest)) {
//				int unformattedQuestNameWidth = Math.max(this.fontRenderer.getStringWidth(unformattedQuestName), 120);
//				int questNameWrappedHeight = this.fontRenderer.getWordWrappedHeight(questDescription,
//						unformattedQuestNameWidth);
//
//				if (QuestUtil.hasQuestUnlocked(this.player, quest)) {
//					questNameWrappedHeight += 12;
//				}
//
//				this.drawGradientRect(mouseXOffset - 3, mouseYOffset - 3, mouseXOffset + unformattedQuestNameWidth + 3,
//						mouseYOffset + questNameWrappedHeight + 3 + 12, -1073741824, -1073741824);
//				this.fontRenderer.drawSplitString(questDescription, mouseXOffset, mouseYOffset + 12,
//						unformattedQuestNameWidth, -6250336);
//
//				if (QuestUtil.hasQuestUnlocked(this.player, quest)) {
//					this.fontRenderer.drawStringWithShadow(I18n.format("quest.taken", new Object[0]),
//							(float) mouseXOffset, (float) (mouseYOffset + questNameWrappedHeight + 4), -7302913);
//				}
//			} else if (requirementCount == 3) {
//				unformattedQuestName = I18n.format("quest.unknown", new Object[0]);
//				int unformattedQuestNameWidth = Math.max(this.fontRenderer.getStringWidth(unformattedQuestName), 120);
//				String unformattedRequires = (new TextComponentTranslation("quest.requires",
//						new Object[] { QuestUtil.getStatName(quest.getParent()) })).getUnformattedText();
//				int wordWrappedHeight = this.fontRenderer.getWordWrappedHeight(unformattedRequires,
//						unformattedQuestNameWidth);
//				this.drawGradientRect(mouseXOffset - 3, mouseYOffset - 3, mouseXOffset + unformattedQuestNameWidth + 3,
//						mouseYOffset + wordWrappedHeight + 12 + 3, -1073741824, -1073741824);
//				this.fontRenderer.drawSplitString(unformattedRequires, mouseXOffset, mouseYOffset + 12,
//						unformattedQuestNameWidth, -9416624);
//			} else if (requirementCount < 3) {
//				int unformattedQuestNameWidth = Math.max(this.fontRenderer.getStringWidth(unformattedQuestName), 120);
//				String unformattedRequires = "";
//
//				if (quest.getParent() != null) {
//					unformattedRequires = (new TextComponentTranslation("quest.requires",
//							new Object[] { QuestUtil.getStatName(quest.getParent()) })).getUnformattedText();
//					unformattedRequires += " " + quest.getParent().getQuestName();
//				}
//
//				int wordWrappedHeight = this.fontRenderer.getWordWrappedHeight(unformattedRequires,
//						unformattedQuestNameWidth);
//				this.drawGradientRect(mouseXOffset - 3, mouseYOffset - 3, mouseXOffset + unformattedQuestNameWidth + 3,
//						mouseYOffset + wordWrappedHeight + 12 + 3, -1073741824, -1073741824);
//				this.fontRenderer.drawSplitString(unformattedRequires, mouseXOffset, mouseYOffset + 12,
//						unformattedQuestNameWidth, -9416624);
//			} else {
//				unformattedQuestName = null;
//			}
//
//			if (unformattedQuestName != null) {
//				this.fontRenderer.drawStringWithShadow(unformattedQuestName, (float) mouseXOffset, (float) mouseYOffset,
//						QuestUtil.canUnlockQuest(this.player, quest) ? -1 : -8355712);
//			}
//		}
//
//		GlStateManager.enableDepth();
//		GlStateManager.enableLighting();
//		RenderHelper.disableStandardItemLighting();
//	}
}
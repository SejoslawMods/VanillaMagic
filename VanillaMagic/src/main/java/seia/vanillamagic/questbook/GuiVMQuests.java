package seia.vanillamagic.questbook;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.util.QuestUtil;
import seia.vanillamagic.util.TextUtil;

/**
 * GUI which shows Quests progress.
 * Remade of the old Achievements GUI.
 */
@SideOnly(Side.CLIENT)
public class GuiVMQuests extends GuiScreen implements IProgressMeter
{
	/**
	 * Is the smallest column used to display a Quest on the GUI.
	 */
	public static int MIN_DISPLAY_COLUMN;
	/**
	 * Is the smallest row used to display a Quest on the GUI.
	 */
	public static int MIN_DISPLAY_ROW;
	/**
	 * Is the biggest column used to display a Quest on the GUI.
	 */
	public static int MAX_DISPLAY_COLUMN;
	/**
	 * Is the biggest row used to display a Quest on the GUI.
	 */
	public static int MAX_DISPLAY_ROW;
	
	private static final int X_MIN = MIN_DISPLAY_COLUMN * 50 - 112;
	private static final int Y_MIN = MIN_DISPLAY_ROW * 50 - 112;
	private static final int X_MAX = MAX_DISPLAY_COLUMN * 50 - 77;
	private static final int Y_MAX = MAX_DISPLAY_ROW * 50 - 77;
	
	/**
	 * Parent screen. (This should be in-game screen).
	 */
	private GuiScreen _parentScreen;
	/**
	 * Player who opened book.
	 */
	private EntityPlayer _player;
	/**
	 * Is Player scrolling the screen
	 */
	private int _scrolling;
	/**
	 * Are the Quests loading
	 */
	private boolean _loadingQuests = true;
	
	protected int imageWidth = 256;
	protected int imageHeight = 202;
	protected int xLastScroll;
	protected int yLastScroll;
	protected float zoom = 1.0F;
	protected double xScrollO;
	protected double yScrollO;
	protected double xScrollP;
	protected double yScrollP;
	protected double xScrollTarget;
	protected double yScrollTarget;
	
	public GuiVMQuests(GuiScreen parentScreen, EntityPlayer playerWhoOpenedBook) 
	{
		this._parentScreen = parentScreen;
		this._player = playerWhoOpenedBook;
		//this._statsManager = PlayerUtil.getStatisticsManager(this._player);
		this.xScrollTarget = (double)(QuestList.get(0).getPosition().getX() * 24 - 70 - 12);
        this.xScrollO = this.xScrollTarget;
        this.xScrollP = this.xScrollTarget;
        this.yScrollTarget = (double)(QuestList.get(0).getPosition().getY() * 24 - 70);
        this.yScrollO = this.yScrollTarget;
        this.yScrollP = this.yScrollTarget;
	}
	
	/**
	 * Main method which is called when GUI is displayed
	 */
	public void initGui()
	{
		this.mc.getConnection().sendPacket(new CPacketClientStatus(CPacketClientStatus.State.REQUEST_STATS));
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, 0, 0, 80, 20, I18n.format("gui.done", new Object[0])));
	}
	
	/**
	 * When button is pressed with Mouse.
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (!this._loadingQuests)
		{
			if (button.id == 1)
			{
				Minecraft.getMinecraft().displayGuiScreen(this._parentScreen);
			}
		}
	}
	
	/**
	 * Draw the screen.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (this._loadingQuests)
		{
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRenderer, "Loading Quests", this.width / 2, this.height / 2, Color.WHITE.getRGB());
		}
		else
		{
			if (Mouse.isButtonDown(0))
			{
				if ((this._scrolling == 0 || this._scrolling == 1))
				{
					if (this._scrolling == 0)
					{
						this._scrolling = 1;
					}
					else
					{
						this.xScrollP -= (double)((float)(mouseX - this.xLastScroll) * this.zoom);
						this.yScrollP -= (double)((float)(mouseY - this.yLastScroll) * this.zoom);
						this.xScrollO = this.xScrollP;
						this.yScrollO = this.yScrollP;
						this.xScrollTarget = this.xScrollP;
						this.yScrollTarget = this.yScrollP;
					}
					this.xLastScroll = mouseX;
					this.yLastScroll = mouseY;
				}
			}
			else
			{
				this._scrolling = 0;
			}
			
			int wheelMovement = Mouse.getDWheel();
			float localZoom = this.zoom;
			
			if (wheelMovement < 0)
			{
				this.zoom += 0.25F;
			}
			else if (wheelMovement > 0)
			{
				this.zoom -= 0.25F;
			}
			
			this.zoom = MathHelper.clamp(this.zoom, 1.0F, 2.0F);
			
			if (this.zoom != localZoom)
			{
				float localImageZoomWidth = localZoom * (float)this.imageWidth;
				float localImageZoomHeight = localZoom * (float)this.imageHeight;
				float imageZoomWidth = this.zoom * (float)this.imageWidth;
				float imageZoomHeight = this.zoom * (float)this.imageHeight;
				this.xScrollP -= (double)((imageZoomWidth - localImageZoomWidth) * 0.5F);
				this.yScrollP -= (double)((imageZoomHeight - localImageZoomHeight) * 0.5F);
				this.xScrollO = this.xScrollP;
				this.yScrollO = this.yScrollP;
				this.xScrollTarget = this.xScrollP;
				this.yScrollTarget = this.yScrollP;
			}
			
			if (this.xScrollTarget < (double)X_MIN)
			{
				this.xScrollTarget = (double)X_MIN;
			}
			
			if (this.yScrollTarget < (double)Y_MIN)
			{
				this.yScrollTarget = (double)Y_MIN;
			}
			
			if (this.xScrollTarget >= (double)X_MAX)
			{
				this.xScrollTarget = (double)(X_MAX - 1);
			}
			
			if (this.yScrollTarget >= (double)Y_MAX)
			{
				this.yScrollTarget = (double)(Y_MAX - 1);
			}
			
			// Draw Default Background - grey background
			this.drawDefaultBackground();
			// Draw Quests - Quest Screen
			this.drawQuestsScreen(mouseX, mouseY, partialTicks);
			// Draw Title - Quests Title
			this.drawCenteredString(this.fontRenderer, "Quests", this.width / 2, 5, Color.WHITE.getRGB());
			// Draw Buttons - Done Button
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	
	/**
	 * Update screen.
	 */
	public void updateScreen()
	{
		if (!this._loadingQuests)
		{
			this.xScrollO = this.xScrollP;
			this.yScrollO = this.yScrollP;
			double xScroll = this.xScrollTarget - this.xScrollP;
			double yScroll = this.yScrollTarget - this.yScrollP;
			
			if (xScroll * xScroll + yScroll * yScroll < 4.0D)
			{
				this.xScrollP += xScroll;
				this.yScrollP += yScroll;
			}
			else
			{
				this.xScrollP += xScroll * 0.85D;
				this.yScrollP += yScroll * 0.85D;
			}
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return !this._loadingQuests;
	}
	
	public void onStatsUpdated() 
	{
		if (this._loadingQuests)
		{
			this._loadingQuests = false;
		}
	}
	
	private TextureAtlasSprite getTexture(Block block)
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getDefaultState());
	}
	
	/**
	 * i -> xScroll
	 * j -> yScroll
	 * k -> centerWidth
	 * l -> centerHeight
	 * i1 -> centerWidthWithOffset
	 * j1 -> centerHeightWithOffset
	 * i2 -> drawBackgroundPosXOffset
	 * j2 -> drawBackgroundPosYOffset
	 * i4 -> drawBackgroundPosX
	 * l3 -> drawBackgroundPosY
	 * l6 -> iconPosX
	 * j7 -> iconPosY
	 */
	public void drawQuestsScreen(int mouseX, int mouseY, float partialTicks)
	{
		int xScroll = MathHelper.floor(this.xScrollO + (this.xScrollP - this.xScrollO) * (double) partialTicks);
        int yScroll = MathHelper.floor(this.yScrollO + (this.yScrollP - this.yScrollO) * (double) partialTicks);

        if (xScroll < X_MIN)
        {
        	xScroll = X_MIN;
        }

        if (yScroll < Y_MIN)
        {
        	yScroll = Y_MIN;
        }

        if (xScroll >= X_MAX)
        {
        	xScroll = X_MAX - 1;
        }

        if (yScroll >= Y_MAX)
        {
        	yScroll = Y_MAX - 1;
        }

        int centerWidth = (this.width - this.imageWidth) / 2;
        int centerHeight = (this.height - this.imageHeight) / 2;
        int centerWidthWithOffset = centerWidth + 16;
        int centerHeightWithOffset = centerHeight + 17;
        this.zLevel = 0.0F;
        GlStateManager.depthFunc(518);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) centerWidthWithOffset, (float) centerHeightWithOffset, -200.0F);
        GlStateManager.scale(1.0F / this.zoom, 1.0F / this.zoom, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        int k1 = xScroll + 288 >> 4;
        int l1 = yScroll + 288 >> 4;
        int drawBackgroundPosXOffset = (xScroll + 288) % 16;
        int drawBackgroundPosYOffset = (yScroll + 288) % 16;
        Random random = new Random();
        float f = 16.0F / this.zoom;
        float f1 = 16.0F / this.zoom;
        
        // Draw Background
        for (int drawBackgroundPosY = 0; (float)drawBackgroundPosY * f - (float)drawBackgroundPosYOffset < 155.0F; ++drawBackgroundPosY)
        {
            float f2 = 0.6F - (float)(l1 + drawBackgroundPosY) / 25.0F * 0.3F;
            GlStateManager.color(f2, f2, f2, 1.0F);

            for (int drawBackgroundPosX = 0; (float)drawBackgroundPosX * f1 - (float)drawBackgroundPosXOffset < 224.0F; ++drawBackgroundPosX)
            {
                random.setSeed((long)(this.mc.getSession().getPlayerID().hashCode() + k1 + drawBackgroundPosX + (l1 + drawBackgroundPosY) * 16));
                int j4 = random.nextInt(1 + l1 + drawBackgroundPosY) + (l1 + drawBackgroundPosY) / 2;
                TextureAtlasSprite textureatlassprite = this.getTexture(Blocks.SAND);

                if (j4 <= 37 && l1 + drawBackgroundPosY != 35)
                {
                    if (j4 == 22)
                    {
                        if (random.nextInt(2) == 0)
                        {
                            textureatlassprite = this.getTexture(Blocks.DIAMOND_ORE);
                        }
                        else
                        {
                            textureatlassprite = this.getTexture(Blocks.REDSTONE_ORE);
                        }
                    }
                    else if (j4 == 10)
                    {
                        textureatlassprite = this.getTexture(Blocks.IRON_ORE);
                    }
                    else if (j4 == 8)
                    {
                        textureatlassprite = this.getTexture(Blocks.COAL_ORE);
                    }
                    else if (j4 > 4)
                    {
                        textureatlassprite = this.getTexture(Blocks.STONE);
                    }
                    else if (j4 > 0)
                    {
                        textureatlassprite = this.getTexture(Blocks.DIRT);
                    }
                }
                else
                {
                    Block block = Blocks.BEDROCK;
                    textureatlassprite = this.getTexture(block);
                }

                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                // Main background drawing method
                this.drawTexturedModalRect(drawBackgroundPosX * 16 - drawBackgroundPosXOffset, drawBackgroundPosY * 16 - drawBackgroundPosYOffset, textureatlassprite, 16, 16);
            }
        }
        
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);

        List<IQuest> questList = QuestList.getQuests();
        for (int i = 0; i < questList.size(); ++i) // j5 -> i
        {
            IQuest quest = questList.get(i);

            if (quest.getParent() != null && questList.contains(quest.getParent()))
            {
                int k5 = quest.getPosition().getX() * 24 - xScroll + 11; // displayColumn
                int l5 = quest.getPosition().getY() * 24 - yScroll + 11; // displayRow
                int j6 = quest.getParent().getPosition().getX() * 24 - xScroll + 11;
                int k6 = quest.getParent().getPosition().getY() * 24 - yScroll + 11;
                boolean flag = QuestUtil.hasQuestUnlocked(this._player, quest);  //this._statsManager.hasAchievementUnlocked(quest);
                boolean flag1 = QuestUtil.canUnlockQuest(this._player, quest);  //this.statFileWriter.canUnlockAchievement(quest);
                int k4 = QuestUtil.countRequirementsUntilAvailable(this._player, quest);

                if (k4 <= 4)
                {
                    int l4 = -16777216;

                    if (flag)
                    {
                        l4 = -6250336;
                    }
                    else if (flag1)
                    {
                        l4 = -16711936;
                    }

                    this.drawHorizontalLine(k5, j6, l5, l4);
                    this.drawVerticalLine(j6, l5, k6, l4);

                    if (k5 > j6)
                    {
                        this.drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
                    }
                    else if (k5 < j6)
                    {
                        this.drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
                    }
                    else if (l5 > k6)
                    {
                        this.drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
                    }
                    else if (l5 < k6)
                    {
                        this.drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
                    }
                }
            }
        }

        IQuest quest = null;
        float f3 = (float)(mouseX - centerWidthWithOffset) * this.zoom;
        float f4 = (float)(mouseY - centerHeightWithOffset) * this.zoom;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();

        for (int i6 = 0; i6 < questList.size(); ++i6)
        {
            IQuest quest2 = questList.get(i6);
            int iconPosX = quest2.getPosition().getX() * 24 - xScroll;
            int iconPosY = quest2.getPosition().getY() * 24 - yScroll;

            if (iconPosX >= -24 && iconPosY >= -24 && (float)iconPosX <= 224.0F * this.zoom && (float)iconPosY <= 155.0F * this.zoom)
            {
                int l7 = QuestUtil.countRequirementsUntilAvailable(this._player, quest2);

                if (QuestUtil.hasQuestUnlocked(this._player, quest2))
                {
                    float f5 = 0.75F;
                    GlStateManager.color(0.75F, 0.75F, 0.75F, 1.0F);
                }
                else if (QuestUtil.canUnlockQuest(this._player, quest2))
                {
                    float f6 = 1.0F;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                }
                else if (l7 < 3)
                {
                    float f7 = 0.3F;
                    GlStateManager.color(0.3F, 0.3F, 0.3F, 1.0F);
                }
                else if (l7 == 3)
                {
                    float f8 = 0.2F;
                    GlStateManager.color(0.2F, 0.2F, 0.2F, 1.0F);
                }
                else
                {
                    if (l7 != 4)
                    {
                        continue;
                    }

                    float f9 = 0.1F;
                    GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
                }

                GlStateManager.enableBlend(); // Forge: Specifically enable blend because it is needed here. And we fix Generic RenderItem's leakage of it.
//                if (quest2.getSpecial())
//                {
             // Spiked Quest background
//                    this.drawTexturedModalRect(iconPosX - 2, iconPosY - 2, 26, 202, 26, 26); 
//                }
//                else
//                {
             // Normal rectangle Quest background
//                    this.drawTexturedModalRect(iconPosX - 2, iconPosY - 2, 0, 202, 26, 26);
//                }
                GlStateManager.disableBlend(); //Forge: Cleanup states we set.

                if (!QuestUtil.canUnlockQuest(this._player, quest2))
                {
                    float f10 = 0.1F;
                    GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
                    //this.itemRender.isNotRenderingEffectsInGUI(false);
                }

                GlStateManager.disableLighting(); //Forge: Make sure Lighting is disabled. Fixes MC-33065
                GlStateManager.enableCull();
                this.itemRender.renderItemAndEffectIntoGUI(quest2.getIcon(), iconPosX + 3, iconPosY + 3);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableLighting();

//                if (!QuestUtil.canUnlockQuest(this._player, quest2))
//                {
//                    this.itemRender.isNotRenderingEffectsInGUI(true);
//                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                if (f3 >= (float)iconPosX && f3 <= (float)(iconPosX + 22) && f4 >= (float)iconPosY && f4 <= (float)(iconPosY + 22))
                {
                    quest = quest2;
                }
            }
        }

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.drawTexturedModalRect(centerWidth, centerHeight, 0, 0, this.imageWidth, this.imageHeight); // Renders all MC images
        this.zLevel = 0.0F;
        GlStateManager.depthFunc(515);
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        // TODO: Temporary solution for Quest names
        int questNameMoveX = 0;
        int questNameMoveY = -15;
        String questName = TextUtil.translateToLocal("quest.name") + ": ";
        if (quest != null)
        {
            String s = quest.getQuestData().getStatName().getUnformattedText();
            String questDescription = quest.getQuestDescription();//.getDescription();
            int i7 = mouseX + 12;
            int k7 = mouseY - 4;
            int i8 = QuestUtil.countRequirementsUntilAvailable(this._player, quest);

            if (QuestUtil.canUnlockQuest(this._player, quest))
            {
                int j8 = Math.max(this.fontRenderer.getStringWidth(s), 120);
                int i9 = this.fontRenderer.getWordWrappedHeight(questDescription, j8);

                if (QuestUtil.hasQuestUnlocked(this._player, quest))
                {
                    i9 += 12;
                }

                this.drawGradientRect(i7 - 3, k7 - 3, i7 + j8 + 3, k7 + i9 + 3 + 12, -1073741824, -1073741824);
                this.fontRenderer.drawSplitString(questDescription, i7, k7 + 12, j8, -6250336);

                if (QuestUtil.hasQuestUnlocked(this._player, quest))
                {
                    this.fontRenderer.drawStringWithShadow(I18n.format("quest.taken", new Object[0]), (float)i7, (float)(k7 + i9 + 4), -7302913);
                }
                
                this.fontRenderer.drawSplitString(questName + quest.getQuestName(), i7 + questNameMoveX, k7 + questNameMoveY, k7, -1); // TODO: Quest Name
            }
            else if (i8 == 3 && quest.getParent() != null)
            {
                s = I18n.format("quest.unknown", new Object[0]);
                int k8 = Math.max(this.fontRenderer.getStringWidth(s), 120);
                this.fontRenderer.drawSplitString(questName + quest.getQuestName(), i7 + questNameMoveX, k7 + questNameMoveY, k8, -1); // TODO: Quest Name
                String s2 = (new TextComponentTranslation("quest.requires", new Object[] {quest.getParent().getQuestData().getStatName()})).getUnformattedText();
                int i5 = this.fontRenderer.getWordWrappedHeight(s2, k8);
                this.drawGradientRect(i7 - 3, k7 - 3, i7 + k8 + 3, k7 + i5 + 12 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawSplitString(s2, i7, k7 + 12, k8, -9416624);
            }
            else if (i8 < 3 && quest.getParent() != null)
            {
                int l8 = Math.max(this.fontRenderer.getStringWidth(s), 120);
                this.fontRenderer.drawSplitString(questName + quest.getQuestName(), i7 + questNameMoveX, k7 + questNameMoveY, l8, -1); // TODO: Quest Name
                String s3 = (new TextComponentTranslation("quest.requires", new Object[] {quest.getParent().getQuestData().getStatName()})).getUnformattedText();
                s3 += " " + quest.getParent().getQuestName();
                int j9 = this.fontRenderer.getWordWrappedHeight(s3, l8);
                this.drawGradientRect(i7 - 3, k7 - 3, i7 + l8 + 3, k7 + j9 + 12 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawSplitString(s3, i7, k7 + 12, l8, -9416624);
            }
            else
            {
                s = null;
            }

            if (s != null)
            {
                this.fontRenderer.drawStringWithShadow(s, (float)i7, (float)k7, QuestUtil.canUnlockQuest(this._player, quest) ? -1 : -8355712);
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        RenderHelper.disableStandardItemLighting();
	}
}
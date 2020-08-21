package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class QuestGui extends Screen {
    private static class QuestTreeNode {
        public Quest quest;
        public Set<QuestTreeNode> children;

        public QuestTreeNode(Quest quest) {
            this.quest = quest;
            this.children = new HashSet<>();
        }

        public void clear() {
            this.quest = null;
            this.children.forEach(QuestTreeNode::clear);
            this.children = null;
        }

        public static QuestTreeNode parseTree(Collection<Quest> quests) {
            Quest rootQuest = quests.stream()
                    .filter(quest -> quest.parent == null)
                    .findFirst()
                    .orElse(new Quest());
            QuestTreeNode rootNode = new QuestTreeNode(rootQuest);
            quests.remove(rootQuest);
            parseTree(rootNode, quests);
            return rootNode;
        }

        private static void parseTree(QuestTreeNode rootNode, Collection<Quest> quests) {
            List<Quest> toRemove = new ArrayList<>();
            quests.stream()
                    .filter(quest -> quest.parent == rootNode.quest)
                    .forEach(quest -> {
                        rootNode.children.add(new QuestTreeNode(quest));
                        toRemove.add(quest);
                    });
            quests.removeAll(toRemove);
            rootNode.children.forEach(childNode -> parseTree(childNode, quests));
        }
    }

    private final int itemStackIconSize = 18;
    private final int questBackgroundColor = new Color(143, 137, 143).getRGB();

    private final int questAchievedOverlayColor = Color.green.getRGB();
    private final int questAvailableOverlayColor = Color.yellow.getRGB();
    private final int questLockedOverlayColor = Color.gray.getRGB();

    private QuestTreeNode rootNode;

    public QuestGui() {
        this(TextUtils.translate("vm.gui.questGuiTitle"));
    }

    protected QuestGui(ITextComponent titleIn) {
        super(titleIn);
    }

    protected void init() {
        this.rootNode = QuestTreeNode.parseTree(QuestRegistry.getQuests());
    }

    public void removed() {
        this.rootNode.clear();
    }

//    public boolean mouseClicked(double mouseX, double mouseY, int keyCode) {
//    }
//
//    public boolean mouseReleased(double mouseX, double mouseY, int keyCode) {
//    }
//
//    public boolean keyPressed(int keySym, int scancode, int p_keyPressed_3_) {
//    }
//
//    public boolean mouseDragged(double mouseX, double mouseY, int keyCode, double deltaX, double deltaY) {
//    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, TextUtils.translate("vm.gui.questGuiTitle").getFormattedText(), this.width / 2, 10, TextFormatting.WHITE.getColor());
        RenderSystem.translatef((float) (this.width / 2), (float) (this.height / 2), 0);
        this.drawQuestTreeNode(this.rootNode);
    }

    /**
     * Draws Quest tree.
     */
    private void drawQuestTreeNode(QuestTreeNode node) {
        this.drawQuest(node.quest);

        node.children.forEach(childNode -> {
            int offsetX = this.getOffsetX(childNode);
            int offsetY = this.getOffsetY(childNode);

            this.drawConnection(childNode, offsetX, offsetY);
            this.drawQuestTreeNode(childNode);
            this.moveBackToParent(offsetX, offsetY);
        });
    }

    /**
     * Moves drawing position back to the center of parent Quest.
     */
    private void moveBackToParent(int offsetX, int offsetY) {
        RenderSystem.translatef(-offsetX, -offsetY, 0);

        boolean straightY = true;

        if (offsetX != 0) {
            RenderSystem.translatef((offsetX > 0 ? -(this.itemStackIconSize / 2) - 1 : (this.itemStackIconSize / 2) + 1), 0, 0);
            straightY = false;
        }

        if (offsetY != 0 && straightY) {
            RenderSystem.translatef(0, (offsetY > 0 ? -(this.itemStackIconSize / 2) : (this.itemStackIconSize / 2)), 0);
        }
    }

    /**
     * Draws connection between the nodes.
     */
    private void drawConnection(QuestTreeNode child, int offsetX, int offsetY) {
        int color = this.getQuestColor(child.quest);
        boolean straightY = true;

        if (offsetX != 0) {
            RenderSystem.translatef((offsetX > 0 ? (this.itemStackIconSize / 2) + 1 : -(this.itemStackIconSize / 2) - 1), 0, 0);
            this.hLine(0, offsetX, 0, color);
            RenderSystem.translatef(offsetX, 0, 0);
            straightY = false;
        }

        if (offsetY != 0) {
            if (straightY) {
                RenderSystem.translatef(0, (offsetY > 0 ? (this.itemStackIconSize / 2) : -(this.itemStackIconSize / 2)), 0);
            }

            this.vLine(0, offsetY, 0, color);
            RenderSystem.translatef(0, offsetY, 0);
        }
    }

    /**
     * @return X offset by which the next node should be rendered.
     */
    private int getOffsetX(QuestTreeNode node) {
        return this.itemStackIconSize * 2 * node.quest.posX;
    }

    /**
     * @return Y offset by which the next node should be rendered.
     */
    private int getOffsetY(QuestTreeNode node) {
        return this.itemStackIconSize * 2 * node.quest.posY;
    }

    /**
     * Draws specified Quest on the current position.
     */
    private void drawQuest(Quest quest) {
        float move = (float)(this.itemStackIconSize / 2);
        RenderSystem.translatef(-move, -move, 0.0F);
        fill(0, 0, this.itemStackIconSize, this.itemStackIconSize, this.questBackgroundColor);
        this.drawQuestOverlay(this.getQuestColor(quest));
        this.drawItemStack(quest.iconStack, 1, 1, this.getQuestText(quest));
        RenderSystem.translatef(move, move, 0);
    }

    /**
     * @return Color describing if the specified Quest is achieved, available or locked.
     */
    private int getQuestColor(Quest quest) {
        PlayerEntity player = this.getMinecraft().player;

        if (PlayerQuestProgressRegistry.hasPlayerGotQuest(player, quest.uniqueName)) {
            return this.questAchievedOverlayColor;
        } else if (PlayerQuestProgressRegistry.canPlayerGetQuest(player, quest.uniqueName)) {
            return this.questAvailableOverlayColor;
        } else {
            return this.questLockedOverlayColor;
        }
    }

    /**
     * @return Displayed text for the specified Quest.
     */
    private String getQuestText(Quest quest) {
        return TextUtils.translate("quest." + quest.uniqueName).getFormattedText();
    }

    /**
     * Draws appropriate color overlay around Quest.
     */
    private void drawQuestOverlay(int color) {
        this.vLine(0, this.itemStackIconSize, 0, color);
        this.hLine(0, this.itemStackIconSize, 0, color);
        RenderSystem.translatef(this.itemStackIconSize, 0, 0);
        this.vLine(0, this.itemStackIconSize, 0, color);
        RenderSystem.translatef(-this.itemStackIconSize, this.itemStackIconSize, 0);
        this.hLine(0, this.itemStackIconSize, 0, color);
        RenderSystem.translatef(0, -this.itemStackIconSize, 0);
    }

    /**
     * Draws ItemStack on the specified coordinates with given text on the top-right.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String text) {
        this.setBlitOffset(200);
        this.itemRenderer.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);

        if (font == null) {
            font = this.font;
        }

        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x + font.getStringWidth(text) + 3, y - 18, "");
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0.0F;
    }
}

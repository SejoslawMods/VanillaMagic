package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class QuestBookScreen extends Screen {
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
                    .get();
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

    public QuestBookScreen() {
        this(TextUtils.translate("vm.gui.questBookTitle"));
    }

    protected QuestBookScreen(ITextComponent titleIn) {
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
        this.drawCenteredString(this.font, TextUtils.translate("vm.gui.questBookTitle").getFormattedText(), this.width / 2, 10, TextFormatting.WHITE.getColor());
        RenderSystem.translatef((float) (this.width / 2), (float) (this.height / 2), 0);
        this.drawQuestTreeNode(this.rootNode, null, 0, 0);
    }

    /**
     * Draws Quest tree.
     */
    private void drawQuestTreeNode(QuestTreeNode node, QuestTreeNode parent, int x, int y) {
        this.drawQuest(node.quest);

        node.children.forEach(childNode -> {
            int zoom = 24;
            int offsetX = childNode.quest.posX * zoom;
            int offsetY = childNode.quest.posY * (zoom / 2);
            int posX = x - offsetX;
            int posY = y - offsetY;

            RenderSystem.translatef(offsetX, offsetY, 0);
            // TODO: Render connection between childNode and node. Where 'node' is a parent and 'childNode' is a child
            this.drawQuestTreeNode(childNode, node, posX, posY);
            RenderSystem.translatef(-offsetX, -offsetY, 0);
        });
    }

    /**
     * Draws specified Quest on a given position.
     */
    private void drawQuest(Quest quest) {
        float move = (float)(this.itemStackIconSize / 2);
        RenderSystem.translatef(-move, -move, 0.0F);
        fill(0, 0, this.itemStackIconSize, this.itemStackIconSize, this.questBackgroundColor);
        this.drawQuestOverlay(this.questAchievedOverlayColor); // TODO: Calculate Quest color (green - achieved, yellow - available, grey - currently locked)
        this.drawItemStack(quest.iconStack, 1, 1, TextUtils.translate("quest." + quest.uniqueName).getFormattedText());
        RenderSystem.translatef(move, move, 0);
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
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x + font.getStringWidth(text) + 3, y - 18, text);
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0.0F;
    }
}

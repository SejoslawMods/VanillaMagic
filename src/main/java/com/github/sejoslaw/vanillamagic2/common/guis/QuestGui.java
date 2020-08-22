package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class QuestGui extends Screen {
    private static class QuestTreeNode {
        private static final int QUEST_ACHIEVED_COLOR = Color.green.getRGB();
        private static final int QUEST_AVAILABLE_COLOR = Color.yellow.getRGB();
        private static final int QUEST_LOCKED_COLOR = Color.gray.getRGB();

        public Quest quest;
        public Set<QuestTreeNode> children;
        public int color;
        public int zIndex;

        public QuestTreeNode(Quest quest, int color, int zIndex) {
            this.quest = quest;
            this.children = new HashSet<>();
            this.color = color;
            this.zIndex = zIndex;
        }

        public void clear() {
            this.quest = null;
            this.children.forEach(QuestTreeNode::clear);
            this.children = null;
            this.color = 0;
        }

        public static QuestTreeNode parseTree(Collection<Quest> quests) {
            Quest rootQuest = quests.stream()
                    .filter(quest -> quest.parent == null)
                    .findFirst()
                    .orElse(new Quest());
            QuestTreeNode rootNode = new QuestTreeNode(rootQuest, getQuestColor(rootQuest), 20);
            quests.remove(rootQuest);
            parseTree(rootNode, quests);
            return rootNode;
        }

        private static void parseTree(QuestTreeNode rootNode, Collection<Quest> quests) {
            List<QuestTreeNode> achievedQuests = new ArrayList<>();
            List<QuestTreeNode> availableQuests = new ArrayList<>();
            List<QuestTreeNode> lockedQuests = new ArrayList<>();

            quests.stream()
                    .filter(quest -> quest.parent == rootNode.quest)
                    .forEach(quest -> {
                        int color = getQuestColor(quest);

                        if (color == QUEST_ACHIEVED_COLOR) {
                            achievedQuests.add(new QuestTreeNode(quest, color, 20));
                        } else if (color == QUEST_AVAILABLE_COLOR) {
                            availableQuests.add(new QuestTreeNode(quest, color, 10));
                        } else {
                            lockedQuests.add(new QuestTreeNode(quest, color, 0));
                        }
                    });

            rootNode.children.addAll(lockedQuests);
            rootNode.children.addAll(availableQuests);
            rootNode.children.addAll(achievedQuests);

            quests.removeAll(achievedQuests.stream().map(node -> node.quest).collect(Collectors.toList()));
            quests.removeAll(availableQuests.stream().map(node -> node.quest).collect(Collectors.toList()));
            quests.removeAll(lockedQuests.stream().map(node -> node.quest).collect(Collectors.toList()));

            rootNode.children.forEach(childNode -> parseTree(childNode, quests));
        }

        private static int getQuestColor(Quest quest) {
            PlayerEntity player = Minecraft.getInstance().player;

            if (PlayerQuestProgressRegistry.hasPlayerGotQuest(player, quest.uniqueName)) {
                return QUEST_ACHIEVED_COLOR;
            } else if (PlayerQuestProgressRegistry.canPlayerGetQuest(player, quest.uniqueName)) {
                return QUEST_AVAILABLE_COLOR;
            } else {
                return QUEST_LOCKED_COLOR;
            }
        }
    }

    private final int itemStackIconSize = 18;
    private final int questBackgroundColor = new Color(143, 137, 143).getRGB();

    private QuestTreeNode rootNode;
    private boolean showQuestNames = false;

    public QuestGui() {
        this(TextUtils.translate("vm.gui.questGui.title"));
    }

    protected QuestGui(ITextComponent titleIn) {
        super(titleIn);
    }

    protected void init() {
        this.rootNode = QuestTreeNode.parseTree(QuestRegistry.getQuests());

        this.addButton(new Button(10, 10, 120, 20, TextUtils.translate("vm.gui.questGui.enableQuestNames").getFormattedText(), button -> {
            String key = this.showQuestNames ? "vm.gui.questGui.enableQuestNames" : "vm.gui.questGui.disableQuestNames";
            button.setMessage(TextUtils.translate(key).getFormattedText());
            this.showQuestNames = !this.showQuestNames;
        }));
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
        
        RenderSystem.translatef((float) (this.width / 2), (float) (this.height / 2), 0);
        this.drawQuestTreeNode(this.rootNode);
        RenderSystem.translatef((float) -(this.width / 2), (float) -(this.height / 2), 0);

        RenderSystem.translatef(0, 0, 40);
        this.drawCenteredString(this.font, TextUtils.translate("vm.gui.questGui.title").getFormattedText(), this.width / 2, 10, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
        RenderSystem.translatef(0, 0, -40);
    }

    /**
     * Draws Quest tree.
     */
    private void drawQuestTreeNode(QuestTreeNode node) {
        this.drawQuest(node);

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
        boolean straightY = true;

        RenderSystem.translatef(0, 0, child.zIndex);

        if (offsetX != 0) {
            RenderSystem.translatef((offsetX > 0 ? (this.itemStackIconSize / 2) + 1 : -(this.itemStackIconSize / 2) - 1), 0, 0);
            this.hLine(0, offsetX, 0, child.color);
            RenderSystem.translatef(offsetX, 0, 0);
            straightY = false;
        }

        if (offsetY != 0) {
            if (straightY) {
                RenderSystem.translatef(0, (offsetY > 0 ? (this.itemStackIconSize / 2) : -(this.itemStackIconSize / 2)), 0);
            }

            this.vLine(0, offsetY, 0, child.color);
            RenderSystem.translatef(0, offsetY, 0);
        }

        RenderSystem.translatef(0, 0, -child.zIndex);
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
    private void drawQuest(QuestTreeNode node) {
        float move = (float)(this.itemStackIconSize / 2);
        RenderSystem.translatef(-move, -move, 30);
        fill(0, 0, this.itemStackIconSize, this.itemStackIconSize, this.questBackgroundColor);
        this.drawQuestOverlay(node.color);
        this.drawItemStack(node.quest.iconStack, 1, 1, this.getQuestText(node.quest));
        RenderSystem.translatef(move, move, -30);
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
        this.itemRenderer.zLevel = 35;
        FontRenderer font = stack.getItem().getFontRenderer(stack);

        if (font == null) {
            font = this.font;
        }

        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x + font.getStringWidth(text) + 3, y - 18, this.showQuestNames ? text : "");
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0;
    }
}

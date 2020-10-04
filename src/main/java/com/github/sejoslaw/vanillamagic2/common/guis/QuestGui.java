package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class QuestGui extends VMGui {
    private static class QuestTreeNode {
        public Quest quest;
        public Set<QuestTreeNode> children;
        public int color;
        public QuestTreeNode parentNode;

        public QuestTreeNode(Quest quest, int color, QuestTreeNode parentNode) {
            this.quest = quest;
            this.children = new HashSet<>();
            this.color = color;
            this.parentNode = parentNode;
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
            QuestTreeNode rootNode = new QuestTreeNode(rootQuest, getQuestColor(rootQuest), null);
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
                            achievedQuests.add(new QuestTreeNode(quest, color, rootNode));
                        } else if (color == QUEST_AVAILABLE_COLOR) {
                            availableQuests.add(new QuestTreeNode(quest, color, rootNode));
                        } else {
                            lockedQuests.add(new QuestTreeNode(quest, color, rootNode));
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

    private static class TooltipDrawer {
        private final QuestGui gui;
        private QuestTreeNode node;

        public TooltipDrawer(QuestGui gui) {
            this.gui = gui;
        }

        public void setup(QuestTreeNode node) {
            this.node = node;
        }

        public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (this.node == null) {
                return;
            }

            this.gui.move(matrixStack, mouseX, mouseY + 20, 0);

            List<ITextComponent> lines = new ArrayList<>();
            this.node.quest.fillTooltip(lines);
            this.gui.func_243308_b(matrixStack, lines, -(this.gui.itemStackIconSize / 2), 0);
            this.node = null;
        }
    }

    public static final int QUEST_ACHIEVED_COLOR = Color.green.getRGB();
    public static final int QUEST_AVAILABLE_COLOR = Color.yellow.getRGB();
    public static final int QUEST_LOCKED_COLOR = Color.gray.getRGB();

    public final int itemStackIconSize = 18;
    public final int questBackgroundColor = new Color(143, 137, 143).getRGB();

    private QuestTreeNode rootNode;
    private TooltipDrawer tooltipDrawer;
    private double zoom;
    private boolean showQuestNames = true;
    private boolean showAllQuests = false;
    private boolean showQuestTooltip = true;

    public QuestGui() {
        this(TextUtils.translate("vm.gui.questGui.title"));
    }

    protected QuestGui(ITextComponent titleIn) {
        super(titleIn);
    }

    protected void init() {
        super.init();

        this.rootNode = QuestTreeNode.parseTree(QuestRegistry.getQuests());
        this.tooltipDrawer = new TooltipDrawer(this);
        this.zoom = 4;

        this.addOptionButton("vm.gui.questGui.enableQuestNames", "vm.gui.questGui.disableQuestNames", "vm.gui.questGui.disableQuestNames", () -> this.showQuestNames, button -> this.showQuestNames = !this.showQuestNames);
        this.addOptionButton("vm.gui.questGui.showAllQuests", "vm.gui.questGui.hideLockedQuests", "vm.gui.questGui.showAllQuests", () -> this.showAllQuests, button -> this.showAllQuests = !this.showAllQuests);
        this.addOptionButton("vm.gui.questGui.enableQuestTooltip", "vm.gui.questGui.disableQuestTooltip", "vm.gui.questGui.disableQuestTooltip", () -> this.showQuestTooltip, button -> this.showQuestTooltip = !this.showQuestTooltip);

        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getMainWindow().getHandle(), this::scroll);
    }

    public void onClose() {
        this.rootNode.clear();
    }

    public void scroll(long window, double offsetX, double offsetY) {
        this.zoom += offsetY > 0 ? 1 : -1;
    }

    protected void renderInnerGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();
        move(matrixStack, (float) this.centerX, (float) this.centerY, 0);
        this.drawQuestTreeNode(matrixStack, this.rootNode, mouseX, mouseY);
        matrixStack.pop();

        if (this.showQuestTooltip) {
            matrixStack.push();
            this.tooltipDrawer.draw(matrixStack, mouseX, mouseY);
            matrixStack.pop();
        } else {
            this.tooltipDrawer.setup(null);
        }
    }

    /**
     * Draws Quest tree.
     */
    public void drawQuestTreeNode(MatrixStack matrixStack, QuestTreeNode node, int mouseX, int mouseY) {
        this.drawQuest(matrixStack, node);
        this.checkTooltip(node, mouseX, mouseY);

        node.children.forEach(childNode -> {
            if (childNode.color == QUEST_LOCKED_COLOR && !this.showAllQuests) {
                return;
            }

            int offsetX = this.getOffsetX(childNode);
            int offsetY = this.getOffsetY(childNode);

            this.drawConnection(matrixStack, childNode, offsetX, offsetY);
            this.drawQuestTreeNode(matrixStack, childNode, mouseX, mouseY);
            this.moveBackToParent(matrixStack, offsetX, offsetY);
        });
    }

    /**
     * Setups Tooltip Drawer.
     */
    public void checkTooltip(QuestTreeNode node, int mouseX, int mouseY) {
        int startCenterX = this.width / 2;
        int startCenterY = this.height / 2;
        int radius = this.itemStackIconSize / 2;

        int deltaX = this.centerX - startCenterX;
        int deltaY = this.centerY - startCenterY;

        int questPosX = this.getTotalOffsetX(node);
        int questPosY = this.getTotalOffsetY(node);

        int movedMousePosX = mouseX - deltaX - startCenterX;
        int movedMousePosY = mouseY - deltaY - startCenterY;

        boolean predicate = (movedMousePosX + radius > questPosX) &&
                            (movedMousePosX - radius < questPosX) &&
                            (movedMousePosY + radius > questPosY) &&
                            (movedMousePosY - radius < questPosY);

        if (predicate) {
            this.tooltipDrawer.setup(node);
        }
    }

    /**
     * Moves drawing position back to the center of parent Quest.
     */
    public void moveBackToParent(MatrixStack matrixStack, int offsetX, int offsetY) {
        move(matrixStack, -offsetX, -offsetY, 0);

        boolean straightY = true;

        if (offsetX != 0) {
            move(matrixStack, (offsetX > 0 ? -(this.itemStackIconSize / 2) - 1 : (this.itemStackIconSize / 2) + 1), 0, 0);
            straightY = false;
        }

        if (offsetY != 0 && straightY) {
            move(matrixStack, 0, (offsetY > 0 ? -(this.itemStackIconSize / 2) : (this.itemStackIconSize / 2)), 0);
        }
    }

    /**
     * Draws connection between the nodes.
     */
    public void drawConnection(MatrixStack matrixStack, QuestTreeNode child, int offsetX, int offsetY) {
        boolean straightY = true;

        if (offsetX != 0) {
            move(matrixStack, (offsetX > 0 ? (this.itemStackIconSize / 2) + 1 : -(this.itemStackIconSize / 2) - 1), 0, 0);
            this.hLine(matrixStack, 0, offsetX, 0, child.color);
            move(matrixStack, offsetX, 0, 0);
            straightY = false;
        }

        if (offsetY != 0) {
            if (straightY) {
                move(matrixStack, 0, (offsetY > 0 ? (this.itemStackIconSize / 2) : -(this.itemStackIconSize / 2)), 0);
            }

            this.vLine(matrixStack, 0, offsetY, 0, child.color);
            move(matrixStack, 0, offsetY, 0);
        }
    }

    /**
     * @return X offset by which the next node should be rendered.
     */
    public int getOffsetX(QuestTreeNode node) {
        return (this.itemStackIconSize * node.quest.posX) * (int) this.zoom;
    }

    /**
     * @return Y offset by which the next node should be rendered.
     */
    public int getOffsetY(QuestTreeNode node) {
        return (this.itemStackIconSize * node.quest.posY) * (int) this.zoom;
    }

    /**
     * Draws specified Quest on the current position.
     */
    public void drawQuest(MatrixStack matrixStack, QuestTreeNode node) {
        float move = (float)(this.itemStackIconSize / 2);
        move(matrixStack, -move, -move, 0);
        fill(matrixStack, 0, 0, this.itemStackIconSize, this.itemStackIconSize, this.questBackgroundColor);
        this.drawQuestOverlay(matrixStack, node.color);
        this.drawItemStack(node.quest.iconStack, this.getTotalOffsetX(node), this.getTotalOffsetY(node), node.quest.getDisplayName());
        move(matrixStack, move, move, 0);
    }

    /**
     * Draws appropriate color overlay around Quest.
     */
    public void drawQuestOverlay(MatrixStack matrixStack, int color) {
        this.vLine(matrixStack, 0, this.itemStackIconSize, 0, color);
        this.hLine(matrixStack, 0, this.itemStackIconSize, 0, color);
        move(matrixStack, this.itemStackIconSize, 0, 0);
        this.vLine(matrixStack, 0, this.itemStackIconSize, 0, color);
        move(matrixStack, -this.itemStackIconSize, this.itemStackIconSize, 0);
        this.hLine(matrixStack, 0, this.itemStackIconSize, 0, color);
        move(matrixStack, 0, -this.itemStackIconSize, 0);
    }

    /**
     * Draws ItemStack on the specified coordinates with given text on the top-right.
     */
    public void drawItemStack(ItemStack stack, int x, int y, String text) {
        x += this.centerX - (this.itemStackIconSize / 2) + 1;
        y += this.centerY - (this.itemStackIconSize / 2) + 1;

        FontRenderer font = stack.getItem().getFontRenderer(stack);

        if (font == null) {
            font = this.font;
        }

        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x + font.getStringWidth(text) + 3, y - 18, this.showQuestNames ? text : "");
    }

    /**
     * @return Total X offset of the given node starting from root.
     */
    public int getTotalOffsetX(QuestTreeNode node) {
        if (node.parentNode == null) {
            return 0;
        }

        int offsetX = this.getOffsetX(node);
        int parentOffsetX = this.getTotalOffsetX(node.parentNode);
        int iconOffsetX = 0;

        if (offsetX != 0) {
            iconOffsetX = (offsetX < 0 ? -(this.itemStackIconSize / 2) - 1 : (this.itemStackIconSize / 2) + 1);
        }

        return offsetX + parentOffsetX + iconOffsetX;
    }

    /**
     * @return Total Y offset of the given node starting from root.
     */
    public int getTotalOffsetY(QuestTreeNode node) {
        if (node.parentNode == null) {
            return 0;
        }

        int offsetY = this.getOffsetY(node);
        int parentOffsetY = this.getTotalOffsetY(node.parentNode);
        int iconOffsetY = 0;

        if (offsetY != 0) {
            iconOffsetY = (offsetY < 0 ? -(this.itemStackIconSize / 2) : (this.itemStackIconSize / 2));
        }

        return offsetY + parentOffsetY + iconOffsetY;
    }
}

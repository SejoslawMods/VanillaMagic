package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public abstract class VMGui extends Screen {
    public static final int TEXT_COLOR = Color.white.getRGB();

    protected int centerX, centerY;

    private int optionButtonOffsetX = 10;
    private int optionButtonOffsetY = 10;

    protected VMGui(ITextComponent titleIn) {
        super(titleIn);
    }

    protected void init() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int keyCode) {
        if (keyCode == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.setDragging(true);
        }

        return super.mouseClicked(mouseX, mouseY, keyCode);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int keyCode) {
        if (keyCode == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.setDragging(false);
        }

        return super.mouseReleased(mouseX, mouseY, keyCode);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int keyCode, double deltaX, double deltaY) {
        if (this.isDragging()) {
            this.centerX += deltaX;
            this.centerY += deltaY;
        }

        return super.mouseDragged(mouseX, mouseY, keyCode, deltaX, deltaY);
    }

    public void move(MatrixStack matrixStack, float deltaX, float deltaY, float deltaZ) {
        matrixStack.translate(deltaX, deltaY, deltaZ);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.getTitle().getString(), this.width / 2, 10, TEXT_COLOR);

        matrixStack.push();
        this.renderInnerGui(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pop();

        matrixStack.push();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pop();
    }

    protected void addOptionButton(String trueTranslationKey, String falseTranslationKey, String defaultValueKey, Supplier<Boolean> getFlag, Consumer<Button> onClick) {
        int optionButtonWidth = 120;
        int optionButtonHeight = 20;

        this.addButton(new Button(this.optionButtonOffsetX, this.optionButtonOffsetY, optionButtonWidth, optionButtonHeight, TextUtils.translate(defaultValueKey), button -> {
            String key = getFlag.get() ? trueTranslationKey : falseTranslationKey;
            button.setMessage(TextUtils.translate(key));
            onClick.accept(button);
        }));

        this.optionButtonOffsetY += 30;
    }

    protected abstract void renderInnerGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

    public static void displayGui(Screen screen) {
        Minecraft.getInstance().displayGuiScreen(screen);
    }
}

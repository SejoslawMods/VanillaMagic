package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class QuestBookScreen extends Screen { // TODO: Design methods and their functionality
    public QuestBookScreen() {
        this(TextUtils.translate("vm.gui.questBookTitle"));
    }

    protected QuestBookScreen(ITextComponent titleIn) {
        super(titleIn);
    }

//    protected void init() {
//        // TODO: Gather Player's Quests data to be displayed.
//    }
//
//    public void removed() {
//        // TODO: Clean gathered data.
//    }
//
//    public boolean mouseClicked(double mouseX, double mouseY, int keyCode) {
//    }
//
//    public boolean mouseReleased(double mouseX, double mouseY, int keyCode) {
//    }
//
//    public boolean keyPressed(int keySym, int scancode, int p_keyPressed_3_) {
//    }
//
//    public void render(int mouseX, int mouseY, float partialTicks) {
//    }
//
//    public boolean mouseDragged(double mouseX, double mouseY, int keyCode, double deltaX, double deltaY) {
//    }
//
//    public void renderBackground() {
//        // TODO: Add rendering (whole screen) stone + ores
//    }
//
//    // TODO: ContainerScreen.drawItemStack
}

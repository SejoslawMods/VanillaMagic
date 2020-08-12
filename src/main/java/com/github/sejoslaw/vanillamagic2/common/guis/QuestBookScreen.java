package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBookScreen extends Screen {
    public QuestBookScreen() {
        this(TextUtils.translate("vm.gui.questBookTitle"));
    }

    protected QuestBookScreen(ITextComponent titleIn) {
        super(titleIn);
    }
}

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
public class QuestBookScreen extends Screen { // TODO: Implement !!!
    public QuestBookScreen() {
        this(TextUtils.translate("vm.gui.questBookTitle"));
    }

    protected QuestBookScreen(ITextComponent titleIn) {
        super(titleIn);
    }
}

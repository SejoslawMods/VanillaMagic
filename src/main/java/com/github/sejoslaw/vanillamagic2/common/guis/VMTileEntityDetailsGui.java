package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.core.VanillaMagic;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class VMTileEntityDetailsGui extends VMGui {
    private final IVMTileEntity tileEntity;

    private int lineHeight;

    protected boolean showTileNbt = false;

    public VMTileEntityDetailsGui(IVMTileEntity tileEntity) {
        super(TextUtils.translate("vm.gui.vmTileEntityGui.title"));
        this.tileEntity = tileEntity;
    }

    protected void init() {
        super.init();

        this.centerX = this.width / 3;
        this.centerY = this.height / 5;

        this.lineHeight = this.font.getWordWrappedHeight(VanillaMagic.MODID, 100);

        this.addOptionButton("vm.gui.vmTileEntityGui.showNbtData", "vm.gui.vmTileEntityGui.hideNbtData", "vm.gui.vmTileEntityGui.showNbtData", () -> this.showTileNbt, button -> this.showTileNbt = !this.showTileNbt);
    }

    protected void renderInnerGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, this.font, TextUtils.buildMessageLine("vm.gui.vmTileEntityGui.name", this.tileEntity.getClass().getSimpleName()), this.centerX, this.centerY, TEXT_COLOR);
        this.nextLine(matrixStack);
        this.renderTileInformation(matrixStack);

        if (this.showTileNbt) {
            this.nextLine(matrixStack);
            this.nextLine(matrixStack);
            this.renderNbtData(matrixStack, mouseX, mouseY);
        }
    }

    private void nextLine(MatrixStack matrixStack) {
        move(matrixStack, 0, this.lineHeight, 0);
    }

    private void renderTileInformation(MatrixStack matrixStack) {
        List<ITextComponent> lines = new ArrayList<>();

        this.tileEntity.addInformation(lines);

        for (ITextComponent line : lines) {
            drawString(matrixStack, this.font, TextUtils.getFormattedText(line), this.centerX, this.centerY, TEXT_COLOR);
            this.nextLine(matrixStack);
        }
    }

    private void renderNbtData(MatrixStack matrixStack, int mouseX, int mouseY) {
        CompoundNBT nbt = this.tileEntity.serializeNBT();

        NbtUtils.forEachEntry(nbt, (depth, key, value) -> {
            final String singleSpace = "    ";
            StringBuilder tab = new StringBuilder();

            for (int i = 0; i < depth; ++i) {
                tab.append(singleSpace);
            }

            String str = tab.toString() + key + ": " + value;
            drawString(matrixStack, this.font, str, this.centerX, this.centerY, TEXT_COLOR);
            this.nextLine(matrixStack);
        });
    }
}

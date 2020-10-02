package com.github.sejoslaw.vanillamagic2.common.guis;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
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

        this.lineHeight = this.font.getWordWrappedHeight("", 10);

        this.addOptionButton("vm.gui.vmTileEntityGui.showNbtData", "vm.gui.vmTileEntityGui.hideNbtData", "vm.gui.vmTileEntityGui.showNbtData", () -> this.showTileNbt, button -> this.showTileNbt = !this.showTileNbt);
    }

    protected void renderInnerGui(int mouseX, int mouseY, float partialTicks) {
        this.drawString(this.font, TextUtils.buildMessageLine("vm.gui.vmTileEntityGui.name", this.tileEntity.getClass().getSimpleName()), this.centerX, this.centerY, TEXT_COLOR);
        this.nextLine();
        this.renderTileInformation();

        if (this.showTileNbt) {
            this.renderNbtData(mouseX, mouseY);
        }
    }

    private void nextLine() {
        move(0, this.lineHeight, 0);
    }

    private void renderTileInformation() {
        List<ITextComponent> lines = new ArrayList<>();

        this.tileEntity.addInformation(lines);

        for (ITextComponent line : lines) {
            // TODO: FIX - modify drawing position, get rendering color (somehow O.o)
            this.drawString(this.font, line.getFormattedText(), this.centerX, this.centerY, TEXT_COLOR);
            this.nextLine();
        }
    }

    private void renderNbtData(int mouseX, int mouseY) {
        CompoundNBT nbt = this.tileEntity.serializeNBT();
//        NodeNbt root = NodeNbt.parse(nbt);
//        NodeNbt.forEach((depth, node) -> { });
    }
}

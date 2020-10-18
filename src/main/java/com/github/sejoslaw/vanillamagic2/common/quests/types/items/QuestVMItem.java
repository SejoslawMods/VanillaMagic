package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestVMItem<TVMItem extends IVMItem> extends QuestCraftOnAltar {
    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        ItemStack baseItem = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("baseItem"));
        this.getVMItem().setBaseItem(baseItem.getItem());

        this.ingredients.add(baseItem);
        this.results.add(this.getVMItem().getStack());
    }

    public String getDisplayName() {
        return TextUtils.getFormattedText("quest." + this.uniqueName);
    }

    public String getDescription() {
        return TextUtils.getFormattedText("quest." + this.uniqueName + ".desc");
    }

    /**
     * @return Corresponding VM Item.
     */
    public abstract TVMItem getVMItem();
}

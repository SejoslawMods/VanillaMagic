package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.common.item.book.BookRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.mobspawnerdrop.MobSpawnerRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * VM Creative Tab.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemGroup extends ItemGroup {
	public VMItemGroup(String label) {
		super(label);
	}

	public ItemStack createIcon() {
		return new ItemStack(Blocks.SPAWNER);
	}

	/**
     * @return Returns if CreativeTab should have search bar.
     */
    public boolean hasSearchBar() {
        return true;
    }

    /**
     * Display all VM Custom Items.
     */
	@OnlyIn(Dist.CLIENT)
    public void fill(NonNullList<ItemStack> list) {
		this.addMinecraftBlocks(list);

        //list = MobSpawnerRegistry.fillList(list);
		list = VMItems.fillList(list);
		list = BookRegistry.fillList(list);
		list = WorldUtil.fillList(list);
		//list = ItemUpgradeRegistry.fillList(list);

        super.fill(list);
    }

    private void addMinecraftBlocks(NonNullList<ItemStack> list) {
		addMinecraftBlock(Blocks.SPAWNER, list);
		addMinecraftBlock(Blocks.COMMAND_BLOCK, list);
		addMinecraftBlock(Blocks.CHAIN_COMMAND_BLOCK, list);
		addMinecraftBlock(Blocks.REPEATING_COMMAND_BLOCK, list);
	}

	private void addMinecraftBlock(Block block, NonNullList<ItemStack> list) {
		block.fillItemGroup(this, list);
	}
}
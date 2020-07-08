package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IBook extends ICustomItem {
	/**
	 * @return Returns the unique NBT Tag for this Book.
	 */
	default String getUniqueNBTName() {
		return BookRegistry.BOOK_NBT_UID;
	}
}
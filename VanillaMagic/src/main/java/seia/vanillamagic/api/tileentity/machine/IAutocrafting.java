package seia.vanillamagic.api.tileentity.machine;

/**
 * This is the connection to TileAutocrafting
 */
public interface IAutocrafting extends IMachine
{
	/**
	 * Sets the currently used crafting slot to the new one.
	 * 
	 * @param slot
	 */
	void setCurrentCraftingSlot(int slot);
	
	/**
	 * @return Returns the currently used slot for crafting. (Default = 0).
	 */
	int getCurrentCraftingSlot();
	
	/**
	 * @return Returns the default slot = 0
	 */
	int getDefaultCraftingSlot();
	
	/**
	 * @return Returns the max slot for TileAutocrafting = 4.<br>
	 * It it 4 to let TileAutocrafting operate on slots:  <0 - 4>
	 */
	int getDefaultMaxCraftingSlot();
}
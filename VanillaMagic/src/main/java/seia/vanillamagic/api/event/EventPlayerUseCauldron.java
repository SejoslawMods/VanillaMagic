package seia.vanillamagic.api.event;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This Event is fired when Player try to use Cauldron in Vanilla Magic-way.
 */
public class EventPlayerUseCauldron extends EventPlayerOnWorld 
{
	private final BlockPos _cauldronPos;
	
	public EventPlayerUseCauldron(EntityPlayer player, World world, BlockPos cauldronPos)
	{
		super(player, world);
		this._cauldronPos = cauldronPos;
	}
	
	/**
	 * @return Returns position of the Cauldron.
	 */
	public BlockPos getCauldronPos()
	{
		return _cauldronPos;
	}
	
	public static class FromInput extends EventPlayerUseCauldron
	{
		private final List<EntityItem> _input;
		
		public FromInput(EntityPlayer player, World world, BlockPos cauldronPos, List<EntityItem> input) 
		{
			super(player, world, cauldronPos);
			this._input = input;
		}
		
		/**
		 * @return Returns list with all input items.
		 */
		public List<EntityItem> getInputItems()
		{
			return _input;
		}
	}
	
	/**
	 * This Event is fired BEFORE Player craft something on Altar.
	 */
	public static class CraftOnAltar extends FromInput
	{
		private final ItemStack[] _result;
		
		public CraftOnAltar(EntityPlayer player, World world, BlockPos cauldronPos,
				List<EntityItem> ingredients, ItemStack[] result) 
		{
			super(player, world, cauldronPos, ingredients);
			this._result = result;
		}
		
		/**
		 * @return Returns array of all ItemStack (results) that will be spawned after the crafting is done.
		 */
		public ItemStack[] getCraftingResult()
		{
			return _result;
		}
	}
	
	/**
	 * This Event is fired when Player try to double ores in Cauldron.
	 */
	public static class OreMultiplier extends FromInput 
	{
		public OreMultiplier(EntityPlayer player, World world, BlockPos cauldronPos, List<EntityItem> oresInCauldron) 
		{
			super(player, world, cauldronPos, oresInCauldron);
		}
	}
	
	/**
	 * This Event is fired when Player try to smelt items on Altar.
	 */
	public static class SmeltOnAltar extends FromInput 
	{
		public SmeltOnAltar(EntityPlayer player, World world, BlockPos cauldronPos, List<EntityItem> itemsToSmelt) 
		{
			super(player, world, cauldronPos, itemsToSmelt);
		}
	}
}
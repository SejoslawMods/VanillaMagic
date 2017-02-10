package seia.vanillamagic.magic.spell;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import seia.vanillamagic.magic.wand.IWand;

/**
 * Base Spell definition.
 */
public interface ISpell 
{
	/**
	 * @return Returns the unique Spell ID.
	 */
	int getSpellID();
	
	/**
	 * @return Returns the human readable name of this Spell.
	 */
	String getSpellName();
	
	/**
	 * @return Returns the unique name of this Spell.
	 */
	String getSpellUniqueName();
	
	/**
	 * @return Returns the Wand that is needed to cast this Spell.
	 */
	IWand getWand();
	
	/**
	 * @return Returns the ItemStack that should be in OffHand when casting this Spell.
	 */
	ItemStack getRequiredStackOffHand();
	
	/**
	 * @param caster Player who cast the Spell.
	 * @param pos Position on which the Spell was casted.
	 * @param face Face of the casted block.
	 * @param hitVec Vector from Player to the Block.
	 * @return Returns TRUE if the Spell was casted correctly.
	 */
	boolean castSpell(
			EntityPlayer caster, 
			@Nullable BlockPos pos, 
			@Nullable EnumFacing face, 
			@Nullable Vec3d hitVec);
	
	/**
	 * @param stackOffHand ItemStack in the Player's OffHand.
	 * @return Returns TRUE if the stackOffHand is the same as required ItemStack.
	 */
	boolean isItemOffHandRightForSpell(ItemStack stackOffHand);
}
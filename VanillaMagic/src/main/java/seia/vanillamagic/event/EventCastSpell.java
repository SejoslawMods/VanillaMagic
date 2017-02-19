package seia.vanillamagic.event;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.magic.spell.ISpell;

/**
 * This {@link Event} is fired BEFORE the caster cast spell.
 */
public class EventCastSpell extends Event
{
	private EntityPlayer caster;
	private ISpell spell;
	
	/** Null - if caster cast spell in air.  */
	@Nullable
	private BlockPos clickedPos;
	/** Null - if caster cast spell in air.  */
	@Nullable
	private EnumFacing clickedFace;
	/** Null - if caster cast spell in air.  */
	@Nullable
	private Vec3d clickingVec;
	
	public EventCastSpell(EntityPlayer caster, BlockPos clickedPos, EnumFacing clickedFace, Vec3d clickingVec, ISpell spell) 
	{
		this.caster = caster;
		this.clickedPos = clickedPos;
		this.clickedFace = clickedFace;
		this.clickingVec = clickingVec;
		this.spell = spell;
	}
	
	public EntityPlayer getCaster()
	{
		return caster;
	}
	
	public ISpell getSpell()
	{
		return spell;
	}
	
	/** Null - if caster cast spell in air.  */
	@Nullable
	public BlockPos getClickedPos()
	{
		return clickedPos;
	}
	
	/** Null - if caster cast spell in air.  */
	@Nullable
	public EnumFacing getClickedFace()
	{
		return clickedFace;
	}
	
	/** Null - if caster cast spell in air.  */
	@Nullable
	public Vec3d getClickedVec()
	{
		return clickingVec;
	}
}
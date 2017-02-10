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
	public final EntityPlayer caster;
	public final ISpell spell;
	
	/** Null - if caster cast spell in air.  */
	@Nullable
	public final BlockPos clickedPos;
	/** Null - if caster cast spell in air.  */
	@Nullable
	public final EnumFacing clickedFace;
	/** Null - if caster cast spell in air.  */
	@Nullable
	public final Vec3d clickingVec;
	
	public EventCastSpell(EntityPlayer caster, BlockPos clickedPos, EnumFacing clickedFace, Vec3d clickingVec, ISpell spell) 
	{
		this.caster = caster;
		this.clickedPos = clickedPos;
		this.clickedFace = clickedFace;
		this.clickingVec = clickingVec;
		this.spell = spell;
	}
}
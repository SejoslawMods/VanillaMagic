package seia.vanillamagic.api.magic;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Registry holding data about all Spells and Wands. <br>
 * <br>
 * To check if 2 Spell are Equal do: spell1.getSpellID() == spell2.getSpellID(); <br>
 * To check if 2 Wand are Equal do: MagicRegistry.areWandsEqual(wand1, wand2); OR compare wand1 and wand2 ItemStacks<br>
 */
public class MagicRegistry
{
	/**
	 * SpellRegistry.class
	 */
	public static final String SPELL_REGISTRY = "seia.vanillamagic.magic.spell.SpellRegistry";
	/**
	 * WandRegistry.class
	 */
	public static final String WAND_REGISTRY = "seia.vanillamagic.magic.wand.WandRegistry";
	
	private MagicRegistry()
	{
	}
	
	/**
	 * @return Returns List which contains all currently registered Spells (exclude Mobs). If something went wrong, will return NULL.
	 * 
	 * @see #getSpellPassive()
	 * @see #getSpellHostile()
	 */
	@Nullable
	public static List<ISpell> getSpells()
	{
		try
		{
			Class<?> spellRegistryClass = Class.forName(SPELL_REGISTRY);
			Method m = spellRegistryClass.getMethod("getSpells");
			return (List<ISpell>) m.invoke(null);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @return Returns List which contains all currently registered Passive Mobs Spells. If something went wrong, will return NULL.
	 */
	@Nullable
	public static List<ISpell> getSpellPassive()
	{
		try
		{
			Class<?> spellRegistryClass = Class.forName(SPELL_REGISTRY);
			Method m = spellRegistryClass.getMethod("getSpellPassive");
			return (List<ISpell>) m.invoke(null);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @return Returns List which contains all currently registered Hostile Mobs Spells. If something went wrong, will return NULL.
	 */
	@Nullable
	public static List<ISpell> getSpellHostile()
	{
		try
		{
			Class<?> spellRegistryClass = Class.forName(SPELL_REGISTRY);
			Method m = spellRegistryClass.getMethod("getSpellHostile");
			return (List<ISpell>) m.invoke(null);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param id Id of the Spell.
	 * 
	 * @return Returns the Spell from given Spell Id.
	 */
	@Nullable
	public static ISpell getSpellById(int id)
	{
		try
		{
			Class<?> spellRegistryClass = Class.forName(SPELL_REGISTRY);
			Method m = spellRegistryClass.getMethod("getSpellById", int.class);
			return (ISpell) m.invoke(null, id);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param player Player to check
	 * 
	 * @return Returns Wand which Player is holding in MainHand. If item in MainHand is not a Wand it will return NULL.
	 */
	@Nullable
	public static IWand isWandInMainHand(EntityPlayer player)
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("isWandInMainHand", EntityPlayer.class);
			return (IWand) m.invoke(null, player);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param player Player to check
	 * 
	 * @return Returns Wand which Player is holding in OffHand. If item in OffHand is not a Wand it will return NULL.
	 */
	@Nullable
	public static IWand isWandInOffHand(EntityPlayer player)
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("isWandInOffHand", EntityPlayer.class);
			return (IWand) m.invoke(null, player);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param stack ItemStack to check.
	 * 
	 * @return Returns Wand from the given ItemStack. If the given ItemStack is not registered as Wand, it will return NULL.
	 */
	@Nullable
	public static IWand getWandByItemStack(ItemStack stack)
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("isWandInOffHand", ItemStack.class);
			return (IWand) m.invoke(null, stack);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * @param wand1 First wand to check
	 * @param wand2 Second wand to check
	 * 
	 * @return Returns TRUE if the given Wands are equal.
	 */
	public static boolean areWandsEqual(IWand wand1, IWand wand2)
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("areWandsEqual", IWand.class, IWand.class);
			return (boolean) m.invoke(null, wand1, wand2);
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * @param wand Wand to check
	 * @param spell Spell to check
	 * 
	 * @return Returns TRUE if the given Wand can cast given Spell. In other words, if the given Spell can be casted with the given Wand.
	 */
	public static boolean isWandRightForSpell(IWand wand, ISpell spell)
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("isWandRightForSpell", IWand.class, ISpell.class);
			return (boolean) m.invoke(null, wand, spell);
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * @return Returns List which contains all currently registered Wands.
	 */
	public static List<IWand> getWands()
	{
		try
		{
			Class<?> wandRegistryClass = Class.forName(WAND_REGISTRY);
			Method m = wandRegistryClass.getMethod("getWands");
			return (List<IWand>) m.invoke(null);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
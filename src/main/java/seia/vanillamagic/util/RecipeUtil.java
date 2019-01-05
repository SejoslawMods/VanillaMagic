package seia.vanillamagic.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.core.VanillaMagic;

/**
 * Class which wraps Minecraft and Forge recipes
 */
public class RecipeUtil
{
	private static int RECIPE_ID = 0;
	
	private RecipeUtil()
	{
	}
	
	public static void addShapelessRecipe(ItemStack output, NonNullList<Ingredient> input)
	{
		ShapelessRecipes rec = new ShapelessRecipes("", output, input);
		rec.setRegistryName(VanillaMagic.MODID, "VMRecipe-" + RECIPE_ID++);
		ForgeRegistries.RECIPES.register(rec);
	}
}
package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.recipes.AltarRecipe;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerCraftable<TQuest extends Quest> extends EventCaller<TQuest> {
    protected List<AltarRecipe> recipes = new ArrayList<>();

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (this.quests.size() > 0 && this.recipes.isEmpty()) {
            this.fillRecipes();
        }

        this.executor.craftOnAltar(event, this.recipes, this::canCraftOnAltar);
    }

    /**
     * @return Should always return true if no additional check is required (i.e. checking for tags); otherwise false.
     */
    public boolean canCraftOnAltar(ItemStack requiredIngredientStack, ItemStack ingredientInCauldronStack) {
        return true;
    }

    /**
     * Executed on the first time to fill the list with available recipes.
     */
    public abstract void fillRecipes();

    protected <T> void fillCrystalRecipesFromRegistry(
            Set<Map.Entry<RegistryKey<T>, T>> entries,
            Function<T, ItemStack> getIngredient,
            Function<T, ItemStack> getResult,
            String displayNameKey,
            Function<Map.Entry<RegistryKey<T>, T>, String> getDisplayName) {
        for (Map.Entry<RegistryKey<T>, T> entry : entries) {
            List<ItemStack> ingredients = new ArrayList<>();
            ingredients.add(new ItemStack(Items.NETHER_STAR));
            ingredients.add(getIngredient.apply(entry.getValue()));

            ItemStack stack = getResult.apply(entry.getValue());
            stack.getOrCreateTag().putString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME, entry.getKey().getLocation().toString());

            String parsedDisplayName = getDisplayName.apply(entry);
            stack.setDisplayName(TextUtils.combine(TextUtils.translate(displayNameKey),
                    " " + (parsedDisplayName == null ? entry.getKey().getLocation().toString() : TextUtils.getFormattedText(parsedDisplayName))));

            List<ItemStack> results = new ArrayList<>();
            results.add(stack);

            this.recipes.add(new AltarRecipe(this.quests.get(0), ingredients, results));
        }
    }
}

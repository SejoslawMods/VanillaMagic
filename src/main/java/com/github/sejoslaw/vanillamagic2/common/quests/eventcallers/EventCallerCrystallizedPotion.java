package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCrystallizedPotion;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCrystallizedPotion extends EventCallerCraftable<QuestCrystallizedPotion> {
    public void fillRecipes() {
        this.fillCrystalRecipesFromRegistry(
                ForgeRegistries.POTION_TYPES.getEntries(),
                (potion) -> PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion),
                (potion) -> PotionUtils.addPotionToItemStack(new ItemStack(Items.NETHER_STAR), potion),
                "vmitem.crystallizedPotion.namePrefix",
                (entry) -> {
                    List<EffectInstance> effects = entry.getValue().getEffects();
                    return effects.size() > 0 ? effects.get(0).getEffectName() : null;
                });
    }

    public boolean canCraftOnAltar(ItemStack requiredIngredientStack, ItemStack ingredientInCauldronStack) {
        return PotionUtils.getPotionFromItem(requiredIngredientStack) == PotionUtils.getPotionFromItem(ingredientInCauldronStack);
    }

    public Potion getPotion(ItemStack stack) {
        if (stack.getTag() == null) {
            return null;
        }

        return ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(stack.getTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME)));
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.executor.onPlayerTickNoHandsCheck(event,
                (player, world) -> {
                    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                        Potion potion = this.getPotion(player.inventory.getStackInSlot(i));

                        if (potion == null || potion == Potions.EMPTY) {
                            continue;
                        }

                        return this.quests.get(0);
                    }

                    return null;
                },
                (player, world, quest) -> {
                    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                        Potion potion = this.getPotion(player.inventory.getStackInSlot(i));

                        if (potion == null || potion == Potions.EMPTY) {
                            continue;
                        }

                        potion.getEffects().forEach(player::addPotionEffect);
                    }
                });
    }
}

package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCrystallizedPotion;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCrystallizedPotion extends EventCallerCraftable<QuestCrystallizedPotion> {
    public void fillRecipes() {
        this.fillCrystalRecipesFromRegistry(
                ForgeRegistries.POTION_TYPES.getEntries(),
                (potion) -> PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion),
                (potion) -> PotionUtils.addPotionToItemStack(new ItemStack(Items.NETHER_STAR), potion),
                "vmitem.crystallizedPotion.namePrefix");
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.executor.onPlayerTick(event, (player, world, quest) -> {
            for (ItemStack stack : player.inventory.mainInventory) {
                Potion potion = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(stack.getOrCreateTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME)));

                if (potion == null) {
                    continue;
                }

                for (EffectInstance effect : potion.getEffects()) {
                    player.addPotionEffect(new EffectInstance(effect.getPotion(), 1000, effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles()));
                }
            }
        });
    }
}

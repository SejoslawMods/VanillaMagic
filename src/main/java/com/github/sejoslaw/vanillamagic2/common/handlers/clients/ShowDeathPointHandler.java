package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.utils.ClientUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class ShowDeathPointHandler extends EventHandler {
    @SubscribeEvent
    public void showDeathPoint(LivingDeathEvent event) {
        this.onLivingDeath(event, (livingEntity, damageSource) -> {
            if(!VMForgeConfig.SHOW_LAST_DEATH_POINT.get() || !(livingEntity instanceof PlayerEntity)) {
                return;
            }

            String positionMessage = " " + TextUtils.getPosition(livingEntity.getEntityWorld(), livingEntity.getPosition());
            ClientUtils.addChatMessage(TextUtils.combine(TextUtils.translate("vm.handler.deathPoint"), positionMessage));
        });
    }
}

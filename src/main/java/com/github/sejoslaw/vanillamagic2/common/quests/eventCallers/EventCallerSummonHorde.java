package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestSummonHorde;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerSummonHorde extends EventCaller<QuestSummonHorde> {
    @SubscribeEvent
    public void spawnHorde(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, face) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) ->
                        this.executor.forQuestWithCheck(
                                (quest) -> quest.level == leftHandStack.getCount(),
                                (quest) -> {
                                    TextUtils.addChatMessage("quest.summonHorde.message");
                                    leftHandStack.grow(-quest.leftHandStack.getCount());

                                    List<EntityType<?>> monsterTypes = EntityUtils.getEntitiesByClassification(EntityClassification.MONSTER);

                                    for (int i = 0; i < quest.level * quest.multiplier; ++i) {
                                        Random rand = new Random();
                                        int quadrant = rand.nextInt(4);

                                        int distanceX = rand.nextInt(quest.distanceToPlayer);
                                        int distanceZ = rand.nextInt(quest.distanceToPlayer);

                                        double newPosX = player.getPosX();
                                        double newPosZ = player.getPosZ();

                                        switch (quadrant) {
                                            case 0:
                                                newPosX += distanceX;
                                                newPosZ += distanceZ;
                                            case 1:
                                                newPosX += distanceX;
                                                newPosZ -= distanceZ;
                                            case 2:
                                                newPosX -= distanceX;
                                                newPosZ += distanceZ;
                                            default:
                                                newPosX -= distanceX;
                                                newPosZ -= distanceZ;
                                        }

                                        int indexId = new Random().nextInt(monsterTypes.size());
                                        Entity entity = monsterTypes.get(indexId).create(world);
                                        entity.setPosition(newPosX, player.getPosY(), newPosZ);

                                        world.addEntity(entity);
                                    }
                                })));
    }
}

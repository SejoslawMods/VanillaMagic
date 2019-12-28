package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToEnd extends Spell {
    public SpellTeleportToEnd(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        try {
            if (caster.dimension == DimensionType.OVERWORLD && TeleportUtil.entityChangeDimension(caster, DimensionType.THE_END) != null) {
                return true;
            } else if (caster.dimension == DimensionType.THE_END) {
                World world = caster.world;
                AxisAlignedBB aabb = new AxisAlignedBB(caster.posX - 256, caster.posY - 256, caster.posZ - 256, caster.posX + 256, caster.posY + 256, caster.posZ + 256);
                List<Entity> entities = world.getEntitiesWithinAABB(EntityType.ENDER_DRAGON, aabb, entity -> true);

                for (int i = 0; i < entities.size(); ++i) {
                    if (entities.get(i) instanceof EnderDragonEntity) {
                        EntityUtil.addChatComponentMessageNoSpam(caster, TextUtil.wrap("You need to kill Dragon !!!"));
                        return false;
                    }
                }

                TeleportUtil.entityChangeDimension(caster, DimensionType.OVERWORLD);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
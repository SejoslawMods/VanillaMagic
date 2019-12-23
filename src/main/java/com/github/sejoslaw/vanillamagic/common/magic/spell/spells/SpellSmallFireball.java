package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSmallFireball extends Spell {
    public SpellSmallFireball(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        if (pos != null) {
            return false;
        }

        World world = caster.world;
        world.playEvent(caster, 1018, new BlockPos((int) caster.posX, (int) caster.posY, (int) caster.posZ), 0);

        Vec3d lookingAt = caster.getLookVec();
        double accelX = lookingAt.getX();
        double accelY = lookingAt.getY();
        double accelZ = lookingAt.getZ();

        FireballEntity fireball = new FireballEntity(world, caster, accelX, accelY, accelZ);

        fireball.shootingEntity = caster;
        fireball.setMotion(0.0D, 0.0D, 0.0D);

        double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        fireball.accelerationX = accelX / d0 * 0.1D;
        fireball.accelerationY = accelY / d0 * 0.1D;
        fireball.accelerationZ = accelZ / d0 * 0.1D;

        world.addEntity(fireball);

        return true;
    }
}
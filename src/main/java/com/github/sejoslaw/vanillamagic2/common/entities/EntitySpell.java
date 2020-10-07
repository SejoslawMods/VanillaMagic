package com.github.sejoslaw.vanillamagic2.common.entities;

import com.github.sejoslaw.vanillamagic2.common.spells.logics.EntitySpellLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntitySpell extends DamagingProjectileEntity implements IRendersAsItem {
    private EntitySpellLogic logic;

    public EntitySpell(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    public EntitySpell withLogic(EntitySpellLogic logic) {
        this.logic = logic;
        return this;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.END_ROD;
    }

    protected void onImpact(RayTraceResult result) {
        this.logic.execute(this, this.world, result);
        this.remove();
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return new ItemStack(Items.AIR);
    }
}

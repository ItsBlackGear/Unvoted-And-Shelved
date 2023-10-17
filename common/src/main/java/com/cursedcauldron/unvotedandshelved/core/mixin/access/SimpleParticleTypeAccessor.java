package com.cursedcauldron.unvotedandshelved.core.mixin.access;

import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleParticleType.class)
public interface SimpleParticleTypeAccessor {
    @Invoker("<init>")
    static SimpleParticleType create(boolean bl) {
        throw new UnsupportedOperationException();
    }
}

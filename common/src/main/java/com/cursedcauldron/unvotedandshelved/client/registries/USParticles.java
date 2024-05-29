package com.cursedcauldron.unvotedandshelved.client.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.mixin.access.SimpleParticleTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

public class USParticles {
    public static final CoreRegistry<ParticleType<?>> PARTICLES = CoreRegistry.create(Registry.PARTICLE_TYPE, UnvotedAndShelved.MOD_ID);

    public static final Supplier<SimpleParticleType> GLOWBERRY_DUST = create("glowberry_dust", false);

    private static Supplier<SimpleParticleType> create(String key, boolean overrideLimiter) {
        return PARTICLES.register(key, () -> SimpleParticleTypeAccessor.create(overrideLimiter));
    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> create(String key, boolean overrideLimiter, ParticleOptions.Deserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> factory) {
        return PARTICLES.register(key, () -> new ParticleType<>(overrideLimiter, deserializer) {
            @Override
            public Codec<T> codec() {
                return factory.apply(this);
            }
        });
    }
}
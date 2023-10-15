package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;
import java.util.function.Supplier;

public class USMemoryModules {
    public static final CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registry.MEMORY_MODULE_TYPE, UnvotedAndShelved.MOD_ID);

    public static final Supplier<MemoryModuleType<Integer>> INTERACTION_COOLDOWN_TICKS = create("interaction_cooldown_ticks", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> HEAD_SPIN_COOLDOWN_TICKS = create("head_spin_cooldown_ticks", Codec.INT);
    public static final Supplier<MemoryModuleType<BlockPos>> INTERACTION_POS = create("interaction_pos");

    public static <T> Supplier<MemoryModuleType<T>> create(String key, Codec<T> codec) {
        return MEMORY_MODULES.register(key, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    public static <T> Supplier<MemoryModuleType<T>> create(String key) {
        return MEMORY_MODULES.register(key, () -> new MemoryModuleType<>(Optional.empty()));
    }
}
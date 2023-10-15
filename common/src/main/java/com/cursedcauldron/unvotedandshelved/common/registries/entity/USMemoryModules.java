package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;
import java.util.function.Supplier;

public class USMemoryModules {
    public static final CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registry.MEMORY_MODULE_TYPE, UnvotedAndShelved.MOD_ID);

    // GLARE
    public static final Supplier<MemoryModuleType<Integer>> GRUMPY_TICKS = create("grumpy_ticks", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> TRACKING_DARKNESS_TICKS = create("tracking_darkness_ticks", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> GLOWBERRIES_GIVEN = create("held_glowberries", Codec.INT);
    public static final Supplier<MemoryModuleType<LivingEntity>> GIVE_GLOWBERRIES = create("gave_glowberries");
    public static final Supplier<MemoryModuleType<BlockPos>> DARKNESS_POS = create("darkness_pos");

    // COPPER GOLEM
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
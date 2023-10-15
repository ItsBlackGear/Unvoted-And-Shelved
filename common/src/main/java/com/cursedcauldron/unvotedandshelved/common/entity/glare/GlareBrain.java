package com.cursedcauldron.unvotedandshelved.common.entity.glare;

import com.cursedcauldron.unvotedandshelved.common.entity.glare.behavior.AerialStroll;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.behavior.TrackDarkness;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USActivities;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USSensors;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;

public class GlareBrain {
    private static final ImmutableList<SensorType<? extends Sensor<? super Glare>>> SENSORS = ImmutableList.of(
            SensorType.NEAREST_PLAYERS,
            USSensors.GLARE_TEMPTATIONS.get()
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORIES = ImmutableList.of(
            USMemoryModules.GLOWBERRIES_GIVEN.get(),
            USMemoryModules.GRUMPY_TICKS.get(),
            USMemoryModules.TRACKING_DARKNESS_TICKS.get(),
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES
    );

    public static Brain.Provider<Glare> provider() {
        return Brain.provider(MEMORIES, SENSORS);
    }

    public static Brain<?> create(Brain<Glare> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addTrackingDarknessActivities(brain);
        brain.setMemory(USMemoryModules.GLOWBERRIES_GIVEN.get(), 0);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<Glare> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
                )
        );
    }

    private static void addIdleActivities(Brain<Glare> brain) {
        brain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(5, 10))),
                        Pair.of(1, new FollowTemptation(entity -> 1.25F)),
                        Pair.of(2, new GateBehavior<>(
                                ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                ImmutableSet.of(),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerialStroll(0.6F), 2),
                                        Pair.of(new RandomStroll(0.6F), 2),
                                        Pair.of(new TrackDarkness(10, 0.6F), 2),
                                        Pair.of(new RunIf<>(Glare::isFlying, new DoNothing(30, 60)), 5),
                                        Pair.of(new RunIf<>(Glare::isOnGround, new DoNothing(30, 60)), 5)
                                )
                        ))
                )
        );
    }

    private static void addTrackingDarknessActivities(Brain<Glare> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(
                USActivities.TRACK_DARKNESS.get(),
                ImmutableList.of(
                        Pair.of(0, new TrackDarkness(20, 0.6F))
                ),
                ImmutableSet.of(
                        Pair.of(USMemoryModules.GIVE_GLOWBERRIES.get(), MemoryStatus.VALUE_PRESENT)
                ),
                ImmutableSet.of(
                        USMemoryModules.GIVE_GLOWBERRIES.get()
                )
        );
    }

    public static void updateActivity(Glare glare) {
        glare.getBrain().setActiveActivityToFirstValid(
                ImmutableList.of(
                        USActivities.TRACK_DARKNESS.get(),
                        Activity.IDLE
                )
        );
    }

    public static Ingredient getTemptations() {
        return Glare.TEMPTATION_ITEM;
    }
}
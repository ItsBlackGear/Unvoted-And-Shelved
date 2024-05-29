package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem;

import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior.FindInteraction;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior.SpinHead;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior.TriggerInteraction;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USActivities;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public class CopperGolemBrain {
    private static final ImmutableList<SensorType<? extends Sensor<? super CopperGolem>>> SENSORS = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.HURT_BY
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORIES = ImmutableList.of(
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.IS_PANICKING,
        USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(),
        USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get(),
        USMemoryModules.INTERACTION_POS.get()
    );

    public static Brain.Provider<CopperGolem> provider() {
        return Brain.provider(MEMORIES, SENSORS);
    }

    public static Brain<?> create(Brain<CopperGolem> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addHeadSpinActivities(brain);
        addInteractionActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<CopperGolem> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new AnimalPanic(0.45F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get()),
                new CountDownCooldownTicks(USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get())
            )
        );
    }

    private static void addIdleActivities(Brain<CopperGolem> brain) {
        brain.addActivityWithConditions(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
                Pair.of(1, new RunOne<>(
                    ImmutableList.of(
                        Pair.of(new RandomStroll(0.4F), 2),
                        Pair.of(new SetWalkTargetFromLookTarget(0.4F, 3), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                    )
                ))
            ),
            ImmutableSet.of(
                Pair.of(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_PRESENT)
            )
        );
    }

    private static void addHeadSpinActivities(Brain<CopperGolem> brain) {
        brain.addActivityWithConditions(
            USActivities.SPIN_HEAD.get(),
            ImmutableList.of(
                Pair.of(0, new SpinHead())
            ),
            ImmutableSet.of(
                Pair.of(
                    USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT
                )
            )
        );
    }

    private static void addInteractionActivities(Brain<CopperGolem> brain) {
        brain.addActivityWithConditions(
            USActivities.INTERACT.get(),
            ImmutableList.of(
                Pair.of(0, new FindInteraction(16)),
                Pair.of(1, new TriggerInteraction())
            ),
            ImmutableSet.of(
                Pair.of(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT
            ))
        );
    }

    public static void updateActivity(CopperGolem golem) {
        golem.getBrain().setActiveActivityToFirstValid(
            ImmutableList.of(
                USActivities.INTERACT.get(),
                USActivities.SPIN_HEAD.get(),
                Activity.IDLE
            )
        );
    }
}
package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior;

import com.cursedcauldron.unvotedandshelved.common.entity.USPoses;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.gameevent.GameEvent;

//<>

public class SpinHead extends Behavior<CopperGolem> {
    private static final IntProvider HEAD_SPIN_COOLDOWN = UniformInt.of(120, 200);
    private int spinningTicks;

    public SpinHead() {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, CopperGolem golem) {
        return golem.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty() && golem.getBrain().getMemory(USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, CopperGolem golem, long gameTime) {
        return golem.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty() && golem.getBrain().getMemory(USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected void start(ServerLevel level, CopperGolem golem, long gameTime) {
        golem.getNavigation().stop();
        golem.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        golem.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel level, CopperGolem golem, long gameTime) {
        int timeLimit = 40 + (golem.getWeatherState().ordinal() * 10);
        int playSoundFrame = 20 + (golem.getWeatherState().ordinal() * 5);

        // Increment the spinningTicks counter until the time limit is reached
        if (this.spinningTicks < timeLimit) {
            this.spinningTicks++;
        } else {
            // Set the head spin cooldown memory to a random value
            golem.getBrain().setMemory(USMemoryModules.HEAD_SPIN_COOLDOWN_TICKS.get(), HEAD_SPIN_COOLDOWN.sample(level.random));
        }

        // Pick a sound and change the pose
        if (this.spinningTicks == playSoundFrame) {
            SoundEvent sound = golem.getHeadSpinSound();
            if (sound != null) {
                golem.playSound(sound, 1.0F, 1.0F);
            }

            golem.gameEvent(GameEvent.ENTITY_SHAKE, golem);
            golem.setPose(USPoses.HEAD_SPIN.get());
        }
    }

    @Override
    protected void stop(ServerLevel level, CopperGolem golem, long gameTime) {
        if (this.spinningTicks >= 1) {
            // Reset the spinningTick counter and change the pose back to spinning
            this.spinningTicks = 0;
            golem.setPose(Pose.STANDING);
        }
    }
}
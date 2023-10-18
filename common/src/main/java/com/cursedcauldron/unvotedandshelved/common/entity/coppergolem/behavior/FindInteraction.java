package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior;

import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class FindInteraction extends Behavior<CopperGolem> {
    private static final IntProvider INTERACTION_COOLDOWN = UniformInt.of(120, 240);
    private final int searchRadius;

    private BlockPos copperPos;
    private BlockPos copperPosBelow;

    public FindInteraction(int searchRadius) {
        super(ImmutableMap.of(
                MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT,
                USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT,
                USMemoryModules.INTERACTION_POS.get(), MemoryStatus.VALUE_ABSENT
        ));
        this.searchRadius = searchRadius;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, CopperGolem golem) {
        return golem.getBrain().getMemory(USMemoryModules.INTERACTION_POS.get()).isEmpty() && golem.getBrain().getMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, CopperGolem golem, long gameTime) {
        return golem.getBrain().getMemory(USMemoryModules.INTERACTION_POS.get()).isEmpty() && golem.getBrain().getMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected void start(ServerLevel level, CopperGolem golem, long gameTime) {
        BlockPos copperPos = this.getCopperPos(level, golem);
        if (copperPos != null) {
            this.copperPos = copperPos;
            this.copperPosBelow = copperPos.below();
        }
    }

    @Override
    protected void tick(ServerLevel level, CopperGolem golem, long gameTime) {
        BlockPos copperPos = this.copperPos;
        BlockPos copperPosBelow = this.copperPosBelow;
        if (copperPos != null && copperPosBelow != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(golem, copperPos, 0.4F, 1);
            Path button = golem.getNavigation().createPath(copperPos, 1);
            Path buttonBelow = golem.getNavigation().createPath(copperPosBelow, 1);

            if (button != null && button.canReach()) {
                golem.getNavigation().moveTo(button, 0.4);
                BlockState copperState = level.getBlockState(copperPos);
                if (golem.blockPosition().closerThan(copperPos, 2) && copperState.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
                    golem.getBrain().setMemory(USMemoryModules.INTERACTION_POS.get(), copperPos);
                    this.copperPos = copperPos;
                }
            } else if (buttonBelow != null && buttonBelow.canReach()) {
                golem.getNavigation().moveTo(buttonBelow, 0.4);
                BlockState copperBelowState = level.getBlockState(copperPosBelow);
                if (golem.blockPosition().closerThan(copperPosBelow, 2) && copperBelowState.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
                    golem.getBrain().setMemory(USMemoryModules.INTERACTION_POS.get(), copperPosBelow);
                    this.copperPosBelow = copperPosBelow;
                }
            } else {
                golem.getBrain().eraseMemory(USMemoryModules.INTERACTION_POS.get());
                golem.getBrain().setMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), INTERACTION_COOLDOWN.sample(level.random));
            }
        }
    }

    private BlockPos getCopperPos(ServerLevel level, CopperGolem golem) {
        int radius = this.searchRadius;
        RandomSource random = level.random;

        List<BlockPos> possibles = Lists.newArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = new BlockPos(golem.getX() + x, golem.getY() + y, golem.getZ() + z);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
                        possibles.add(pos);
                    }
                }
            }
        }

        if (possibles.isEmpty()) {
            golem.getBrain().setMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), INTERACTION_COOLDOWN.sample(random));
            return null;
        } else {
            return possibles.get(random.nextInt(possibles.size()));
        }
    }
}
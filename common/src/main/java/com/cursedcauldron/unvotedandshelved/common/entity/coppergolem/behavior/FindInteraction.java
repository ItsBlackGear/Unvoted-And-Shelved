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
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The Copper Golem is programmed to find a suitable location for interaction within a specified radius.
 * It searches for locations where it can interact with blocks. Once a location is found,
 * the Copper Golem moves to that location and performs the interaction.
 * <p>
 * The behavior includes checking if the Copper Golem already has a suitable interaction location in memory,
 * If it doesn't it searches for a new location and ensures it reaches that location by setting a walking
 * target. If the Golem can't reach the location, it will look for an alternative nearby location.
 * If no suitable locations are found, the Golem resets its memory and enters a cooldown period.
 */
public class FindInteraction extends Behavior<CopperGolem> {
    private static final IntProvider INTERACTION_COOLDOWN = UniformInt.of(120, 240);
    private final int searchRadius;

    private BlockPos interactionPosAbove;
    private BlockPos interactionPosBelow;

    public FindInteraction(int searchRadius) {
        super(ImmutableMap.of(
                MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT,
                USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT,
                USMemoryModules.INTERACTION_POS.get(), MemoryStatus.VALUE_ABSENT
        ));
        this.searchRadius = searchRadius;
    }

    /**
     * Checks if the Copper Golem has no interaction memory, which includes information about its
     * current interaction location and cooldown.
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, CopperGolem golem) {
        return this.hasNoInteractionMemory(golem);
    }

    /**
     * Checks if the Copper Golem can continue to use this behavior, which depends on whether it has
     * interaction memory or not.
     */
    @Override
    protected boolean canStillUse(ServerLevel level, CopperGolem golem, long gameTime) {
        return this.hasNoInteractionMemory(golem);
    }

    /**
     * Attempts to find a suitable iteration position and sets up the locations for interaction
     * above and below the found position.
     */
    @Override
    protected void start(ServerLevel level, CopperGolem golem, long gameTime) {
        BlockPos interactionPosition = this.getInteractionPosition(level, golem);
        if (interactionPosition != null) {
            this.interactionPosAbove = interactionPosition;
            this.interactionPosBelow = interactionPosition.below();
        }
    }

    /**
     * Handles the movement and path creation to the interaction locations above and below.
     * If the Golem can't reach the locations, it resets its memory.
     */
    @Override
    protected void tick(ServerLevel level, CopperGolem golem, long gameTime) {
        BlockPos interactionPosAbove = this.interactionPosAbove;
        BlockPos interactionPosBelow = this.interactionPosBelow;
        if (interactionPosAbove != null && interactionPosBelow != null) {
            // Set the walking and looking target for the golem
            BehaviorUtils.setWalkAndLookTargetMemories(golem, interactionPosAbove, 0.4F, 1);

            // Create paths for both interaction positions
            Path pathAbove = golem.getNavigation().createPath(interactionPosAbove, 1);
            Path pathBelow = golem.getNavigation().createPath(interactionPosBelow, 1);

            // Check if the Copper Golem can reach any of the interaction positions.
            if (pathAbove != null && pathAbove.canReach()) {
                this.interactionPosAbove = this.calculatePosition(level, golem, interactionPosAbove, pathAbove);
            } else if (pathBelow != null && pathBelow.canReach()) {
                this.interactionPosBelow = this.calculatePosition(level, golem, interactionPosBelow, pathBelow);
            } else {
                // Reset memory and set a cooldown period
                this.resetInteractions(golem.getBrain(), level.random);
            }
        }
    }

    /**
     * Calculates the interaction position for the Copper Golem based on a provided path.
     * It moves the Golem to the path and checks if it's near an interactable block.
     */
    private @Nullable BlockPos calculatePosition(ServerLevel level, CopperGolem golem, BlockPos pos, Path path) {
        // Move the golem to the path and check if it's near an interactable block
        golem.getNavigation().moveTo(path, 0.4);
        BlockState state = level.getBlockState(pos);
        if (golem.blockPosition().closerThan(pos, 2) && state.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
            golem.getBrain().setMemory(USMemoryModules.INTERACTION_POS.get(), pos);
            return pos;
        }

        return null;
    }

    /**
     * Determines a suitable interaction position for the Copper Golem within its search radius.
     * It checks nearby positions for interactable blocks.
     */
    private BlockPos getInteractionPosition(ServerLevel level, CopperGolem golem) {
        int radius = this.searchRadius;
        RandomSource random = level.random;

        // Create a list to store possible interaction positions.
        List<BlockPos> possibles = Lists.newArrayList();

        // Iterate over positions within the specified search radius.
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = new BlockPos(golem.getX() + x, golem.getY() + y, golem.getZ() + z);
                    BlockState state = level.getBlockState(pos);

                    // Check if the block at this position is interactable
                    if (state.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
                        possibles.add(pos);
                    }
                }
            }
        }

        if (possibles.isEmpty()) {
            // No suitable interactable blocks found, set a cooldown period
            this.resetInteractions(golem.getBrain(), random);
            return null;
        } else {
            // Return a random interactable position
            return possibles.get(random.nextInt(possibles.size()));
        }
    }

    /**
     * Checks if the Copper Golem has no interaction memory, which includes information about its
     * current interaction location and cooldown.
     */
    private boolean hasNoInteractionMemory(CopperGolem golem) {
        return golem.getBrain().getMemory(USMemoryModules.INTERACTION_POS.get()).isEmpty() &&
                golem.getBrain().getMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get()).isEmpty();
    }

    /**
     * Resets the Copper Golem's interaction memories and sets a new cooldown period.
     */
    private void resetInteractions(Brain<CopperGolem> brain, RandomSource random) {
        brain.eraseMemory(USMemoryModules.INTERACTION_POS.get());
        brain.setMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), INTERACTION_COOLDOWN.sample(random));
    }
}
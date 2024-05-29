package com.cursedcauldron.unvotedandshelved.common.entity.glare.behavior;

import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class TrackDarkness extends Behavior<Glare> {
    private BlockPos darknessPos;
    private final int range;
    private final float speed;

    public TrackDarkness(int range, float speed) {
        super(ImmutableMap.of(
            MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Glare glare) {
        return !glare.isInWaterOrBubble() && glare.getBrain().getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get() >= 1;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Glare glare, long gameTime) {
        return glare.getBrain().getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get() >= 1 && this.darknessPos != null;
    }

    @Override
    protected void start(ServerLevel level, Glare glare, long gameTime) {
        this.getDarknessPosition(level, glare);
        if (this.darknessPos != null) {
            Brain<Glare> brain = glare.getBrain();
            BlockPos groundPos = this.darknessPos.below(2);
            if (level.isInWorldBounds(this.darknessPos) && level.getBlockState(this.darknessPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(this.darknessPos) && level.getBlockState(this.darknessPos).isPathfindable(level, this.darknessPos, PathComputationType.LAND) && ((level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && level.getBrightness(LightLayer.SKY, this.darknessPos) == 0) || (level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && level.isNight() || level.isThundering()))) {
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
                    int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get();
                    boolean bl = this.pathfindDirectlyTowards(this.darknessPos, glare);
                    if (bl) {
                        BlockPos pos = new BlockPos(this.darknessPos);
                        BehaviorUtils.setWalkAndLookTargetMemories(glare, this.getNearbyPos(glare, pos), this.speed, 3);
                        if (glare.blockPosition().closerThan(this.darknessPos, 3)) {
                            glare.setLightBlock(pos);
                            glare.gameEvent(GameEvent.BLOCK_PLACE, glare);
                            glare.setHeldGlowberries(i - 1);
                            this.darknessPos = null;
                        }
                    } else {
                        this.darknessPos = null;
                    }
                }
            } else {
                this.darknessPos = null;
            }
        }
    }

    @Override
    protected void tick(ServerLevel level, Glare glare, long gameTime) {
        if (this.darknessPos != null) {
            Brain<Glare> brain = glare.getBrain();
            BlockPos groundPos = this.darknessPos.below(2);
            if (level.isInWorldBounds(this.darknessPos) && level.getBlockState(this.darknessPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(this.darknessPos) && level.getBlockState(this.darknessPos).isPathfindable(level, this.darknessPos, PathComputationType.LAND) && ((level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && level.getBrightness(LightLayer.SKY, this.darknessPos) == 0) || (level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && (level.isNight() || level.isThundering())))) {
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
                    int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get();
                    boolean bl = this.pathfindDirectlyTowards(this.darknessPos, glare);
                    if (bl) {
                        BlockPos pos = new BlockPos(this.darknessPos);
                        BehaviorUtils.setWalkAndLookTargetMemories(glare, this.getNearbyPos(glare, pos), this.speed, 3);
                        if (glare.blockPosition().closerThan(this.darknessPos, 3)) {
                            glare.setLightBlock(pos);
                            glare.gameEvent(GameEvent.BLOCK_PLACE, glare);
                            glare.setHeldGlowberries(i - 1);
                            this.darknessPos = null;
                        }
                    } else {
                        this.darknessPos = null;
                    }
                }
            } else {
                this.darknessPos = null;
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, Glare glare, long gameTime) {
        if (this.darknessPos != null) {
            Brain<Glare> brain = glare.getBrain();
            BlockPos groundPos = this.darknessPos.below(2);
            if (level.isInWorldBounds(this.darknessPos) && level.getBlockState(this.darknessPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(this.darknessPos) && level.getBlockState(this.darknessPos).isPathfindable(level, this.darknessPos, PathComputationType.LAND) && ((level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && level.getBrightness(LightLayer.SKY, this.darknessPos) == 0) || (level.getBrightness(LightLayer.BLOCK, this.darknessPos) == 0 && (level.isNight() || level.isThundering())))) {
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
                    int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get();
                    boolean bl = this.pathfindDirectlyTowards(this.darknessPos, glare);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darknessPos);
                        BehaviorUtils.setWalkAndLookTargetMemories(glare, this.getNearbyPos(glare, blockPos), this.speed, 3);
                        if (glare.blockPosition().closerThan(this.darknessPos, 3)) {
                            glare.setLightBlock(blockPos);
                            glare.gameEvent(GameEvent.BLOCK_PLACE, glare);
                            glare.setHeldGlowberries(i - 1);
                            this.darknessPos = null;
                        }
                    } else {
                        this.darknessPos = null;
                    }
                }
            } else {
                this.darknessPos = null;
            }
        }
    }

    private boolean pathfindDirectlyTowards(BlockPos pos, Glare glare) {
        glare.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
        return glare.getNavigation().getPath() != null && glare.getNavigation().getPath().canReach();
    }

    private boolean isValidSpawnPos(BlockPos blockPos, ServerLevel level) {
        BlockState blockState = level.getBlockState(blockPos);
        FluidState fluidState = level.getFluidState(blockPos);
        return !blockState.is(Blocks.VINE) &&
                !blockState.is(BlockTags.LEAVES) &&
                !blockState.is(BlockTags.CAVE_VINES) &&
                !blockState.isAir() &&
                !fluidState.is(Fluids.WATER) &&
                !fluidState.is(Fluids.FLOWING_WATER);
    }

    private void getDarknessPosition(ServerLevel level, Glare glare) {
        if (this.darknessPos == null) {
            for (int x = this.getRandomNumber(-this.range, this.range); x <= this.getRandomNumber(-this.range, this.range); x++) {
                for (int y = this.getRandomNumber(-this.range, this.range); y <= this.getRandomNumber(-this.range, this.range); y++) {
                    for (int z = this.getRandomNumber(-this.range, this.range); z <= this.getRandomNumber(-this.range, this.range); z++) {
                        BlockPos entityPos = glare.blockPosition();
                        BlockPos blockPos2 = new BlockPos(entityPos).offset(x, y, z);
                        BlockPos spacePos = blockPos2.below();
                        BlockPos groundPos = spacePos.below();
                        if (level.isInWorldBounds(blockPos2) && this.isValidSpawnPos(groundPos, level) && level.isEmptyBlock(spacePos) && level.isEmptyBlock(blockPos2) && level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND) && ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) || (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && (level.isNight() || level.isThundering())))) {
                            glare.getBrain().setMemory(USMemoryModules.DARKNESS_POS.get(), blockPos2);
                            this.darknessPos = blockPos2;
                            return;
                        }
                    }
                }
            }
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private int getRandomOffset(RandomSource random) {
        return random.nextInt(3) - 1;
    }

    // Get a nearby position relative to a given position.
    private BlockPos getNearbyPos(Glare glare, BlockPos blockPos) {
        RandomSource random = glare.level.getRandom();
        return blockPos.offset(getRandomOffset(random), 0, getRandomOffset(random));
    }
}
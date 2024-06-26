package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.behavior;

import com.cursedcauldron.unvotedandshelved.common.entity.USPoses;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class TriggerInteraction extends Behavior<CopperGolem> {
    private static final IntProvider INTERACTION_COOLDOWN = UniformInt.of(120, 240);
    private int interactionTicks;

    public TriggerInteraction() {
        super(ImmutableMap.of(
            USMemoryModules.INTERACTION_POS.get(), MemoryStatus.VALUE_PRESENT,
            MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, CopperGolem golem) {
        return this.canInteractWithPosition(golem);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, CopperGolem golem, long gameTime) {
        return this.canInteractWithPosition(golem);
    }

    @Override
    protected void start(ServerLevel level, CopperGolem golem, long gameTime) {
        Optional<BlockPos> interactionPos = golem.getBrain().getMemory(USMemoryModules.INTERACTION_POS.get());
        interactionPos.ifPresent(pos -> {
            BlockState state = level.getBlockState(pos);

            // Check if the golem is close enough to interact with the button or lever
            boolean isCloseEnough = golem.blockPosition().closerThan(pos, 1.75);

            if (isCloseEnough && state.is(USBlockTags.COPPER_GOLEM_INTERACTABLES)) {
                AttachFace face = state.getValue(BlockStateProperties.ATTACH_FACE);
                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                golem.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos.relative(direction.getOpposite())));

                // Set the golem's pose based on the block's attachment face
                golem.setPose(switch (face) {
                    case FLOOR -> USPoses.INTERACT_BELOW.get();
                    case CEILING -> USPoses.INTERACT_ABOVE.get();
                    default -> USPoses.INTERACT.get();
                });

                // Perform the appropriate interaction for the block
                this.triggerInteraction(level, pos, state);
            }
        });
    }

    @Override
    protected void tick(ServerLevel level, CopperGolem golem, long gameTime) {
        if (this.interactionTicks < 60) {
            this.interactionTicks++;
        } else {
            golem.getBrain().eraseMemory(USMemoryModules.INTERACTION_POS.get());
            golem.getBrain().setMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get(), INTERACTION_COOLDOWN.sample(level.getRandom()));
            golem.setPose(Pose.STANDING);
        }
    }

    @Override
    protected void stop(ServerLevel level, CopperGolem golem, long gameTime) {
        if (this.interactionTicks >= 1) {
            this.interactionTicks = 0;
            golem.setPose(Pose.STANDING);
        }
    }

    private void triggerInteraction(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof ButtonBlock button) {
            if (!button.defaultBlockState().getValue(BlockStateProperties.POWERED)) {
                button.press(state, level, pos);

                SoundEvent sound;
                if (state.is(BlockTags.WOODEN_BUTTONS)) {
                    sound = SoundEvents.WOODEN_BUTTON_CLICK_ON;
                } else if (state.is(USBlockTags.COPPER_BUTTONS)) {
                    sound = USSoundEvents.COPPER_CLICK.get();
                } else {
                    sound = SoundEvents.STONE_BUTTON_CLICK_ON;
                }

                level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.3F, 0.6F);
            }
        } else if (state.getBlock() instanceof LeverBlock lever) {
            lever.pull(state, level, pos);
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
        }
    }

    private boolean canInteractWithPosition(CopperGolem golem) {
        return golem.getBrain().getMemory(USMemoryModules.INTERACTION_POS.get()).isPresent() &&
                golem.getBrain().getMemory(USMemoryModules.INTERACTION_COOLDOWN_TICKS.get()).isEmpty();
    }
}
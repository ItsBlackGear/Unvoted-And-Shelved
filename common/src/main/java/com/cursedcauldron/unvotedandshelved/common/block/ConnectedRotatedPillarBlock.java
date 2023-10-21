package com.cursedcauldron.unvotedandshelved.common.block;

import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ConnectedRotatedPillarBlock extends RotatedPillarBlock {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public ConnectedRotatedPillarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(CONNECTED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        BlockPos abovePos = this.getDirectionalPos(state, pos.above(), pos.east(), pos.north());
        BlockPos belowPos = this.getDirectionalPos(state, pos.below(), pos.west(), pos.south());
        BlockState aboveState = level.getBlockState(abovePos);
        BlockState belowState = level.getBlockState(belowPos);

        if (this.checkForNeighborPillars(state, aboveState)) {
            level.setBlockAndUpdate(pos, state.setValue(CONNECTED, true));
        } else {
            level.setBlockAndUpdate(pos, state.setValue(CONNECTED, false));
        }

        if (this.checkForNeighborPillars(state, belowState)) {
            level.setBlockAndUpdate(belowPos, belowState.setValue(CONNECTED, true));
        }

        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private boolean checkForNeighborPillars(BlockState state, BlockState neighborState) {
        return neighborState.is(USBlockTags.COPPER_PILLARS) && neighborState.getValue(AXIS) == state.getValue(AXIS);
    }

    private BlockPos getDirectionalPos(BlockState state, BlockPos pos, BlockPos pos1, BlockPos pos2) {
        return (state.getValue(AXIS) == Direction.Axis.Y) ? pos : (state.getValue(AXIS) == Direction.Axis.X) ? pos1 : pos2;
    }
}
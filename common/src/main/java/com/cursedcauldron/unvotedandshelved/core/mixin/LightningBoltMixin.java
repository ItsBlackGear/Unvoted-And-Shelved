package com.cursedcauldron.unvotedandshelved.core.mixin;

import com.cursedcauldron.unvotedandshelved.common.block.resource.USWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {
    @Shadow
    private static void randomWalkCleaningCopper(Level level, BlockPos pos, BlockPos.MutableBlockPos mutable, int steps) {}

    @Inject(method = "clearCopperOnLightningStrike", at = @At("TAIL"))
    private static void us$clearCopperOnLightningStrike(Level level, BlockPos pos, CallbackInfo ci) {
        BlockState strikeState = level.getBlockState(pos);

        BlockPos targetPos;
        BlockState targetState;

        if (strikeState.is(Blocks.LIGHTNING_ROD)) {
            targetPos = pos.relative(strikeState.getValue(LightningRodBlock.FACING).getOpposite());
            targetState = level.getBlockState(targetPos);
        } else {
            targetPos = pos;
            targetState = strikeState;
        }

        if (targetState.getBlock() instanceof USWeatheringCopper) {
            level.setBlockAndUpdate(targetPos, USWeatheringCopper.getFirst(level.getBlockState(targetPos)));
            BlockPos.MutableBlockPos mutable = pos.mutable();
            int stepCount = level.random.nextInt(3) + 3;

            for (int step = 0; step < stepCount; step++) {
                int count = level.random.nextInt(8) + 1;
                randomWalkCleaningCopper(level, targetPos, mutable, count);
            }
        }
    }

    @Inject(method = "randomStepCleaningCopper", at = @At("TAIL"), cancellable = true)
    private static void us$RandomStepCleaningCopper(Level level, BlockPos pos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        for (BlockPos neighborPos : BlockPos.randomInCube(level.random, 10, pos, 1)) {
            BlockState neighborState = level.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof USWeatheringCopper) {
                USWeatheringCopper.getPrevious(neighborState).ifPresent(state -> level.setBlockAndUpdate(neighborPos, state));
                level.levelEvent(3002, neighborPos, -1);
                cir.setReturnValue(Optional.of(neighborPos));
            }
        }
    }
}
package com.cursedcauldron.unvotedandshelved.common.entity.glare.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AerialStroll extends RandomStroll {
    public AerialStroll(float speed) {
        super(speed);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob) {
        return mob.getNavigation().isDone() && mob.getRandom().nextInt(10) == 0;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob mob, long gameTime) {
        return mob.getNavigation().isInProgress();
    }

    @Override
    protected void start(ServerLevel level, PathfinderMob mob, long gameTime) {
        Vec3 position = this.findAerialStrollPosition(mob);
        if (position != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(mob, new BlockPos(position), 0.6F, 3);
        }
    }

    @Nullable
    private Vec3 findAerialStrollPosition(PathfinderMob mob) {
        Vec3 viewVector = mob.getViewVector(0.0F);
        Vec3 aerialPosition = HoverRandomPos.getPos(mob, 8, 7, viewVector.x, viewVector.z, (float)Math.PI / 2, 3, 1);
        
        if (aerialPosition != null) {
            BlockPos foundPosition = new BlockPos(aerialPosition);
            if (mob.level.getBlockState(foundPosition).isPathfindable(mob.level, mob.getOnPos(), PathComputationType.LAND)) {
                return aerialPosition;
            }
        }

        return null;
    }
}
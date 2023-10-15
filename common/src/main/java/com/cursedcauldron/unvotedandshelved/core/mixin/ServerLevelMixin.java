package com.cursedcauldron.unvotedandshelved.core.mixin;

import com.cursedcauldron.unvotedandshelved.core.data.tags.USEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Unique private final ServerLevel instance = ServerLevel.class.cast(this);

    @Inject(method = "findLightningTargetAround", at = @At("TAIL"), cancellable = true)
    private void us$findLightningTargetAround(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        BlockPos heightmap = this.instance.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        AABB boundingBox = new AABB(heightmap, new BlockPos(heightmap.getX(), this.instance.getMaxBuildHeight(), heightmap.getZ())).inflate(128.0D);
        List<LivingEntity> entities = this.instance.getEntitiesOfClass(LivingEntity.class, boundingBox, entity -> {
            return entity != null && entity.isAlive() && this.instance.canSeeSky(entity.blockPosition()) && entity.getType().is(USEntityTypeTags.REACHABLE_BY_LIGHTNING);
        });

        if (!entities.isEmpty()) {
            cir.setReturnValue(entities.get(this.instance.random.nextInt(entities.size())).blockPosition().above());
        }
    }
}
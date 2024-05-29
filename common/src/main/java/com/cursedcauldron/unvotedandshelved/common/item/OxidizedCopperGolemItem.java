package com.cursedcauldron.unvotedandshelved.common.item;

import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.OxidizedCopperGolem;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OxidizedCopperGolemItem extends Item {
    public OxidizedCopperGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction direction = context.getClickedFace();
        
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            Level level = context.getLevel();
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            BlockPos pos = placeContext.getClickedPos();
            ItemStack stack = context.getItemInHand();
            Vec3 position = Vec3.atBottomCenterOf(pos);
            AABB boundingBox = USEntities.OXIDIZED_COPPER_GOLEM.get().getDimensions().makeBoundingBox(position.x(), position.y(), position.z());
            
            if (level.noCollision(null, boundingBox) && level.getEntities(null, boundingBox).isEmpty()) {
                if (level instanceof ServerLevel server) {
                    OxidizedCopperGolem golem = USEntities.OXIDIZED_COPPER_GOLEM.get().create(
                        server,
                        stack.getTag(),
                        null,
                        context.getPlayer(),
                        pos,
                        MobSpawnType.SPAWN_EGG,
                        true,
                        true
                    );
                    
                    if (golem == null) {
                        return InteractionResult.FAIL;
                    }

                    float yRot = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    golem.moveTo(golem.getX(), golem.getY(), golem.getZ(), yRot, 0.0F);
                    server.addFreshEntityWithPassengers(golem);
                    
                    level.playSound(null, golem.getX(), golem.getY(), golem.getZ(), SoundEvents.COPPER_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, golem.position());
                    
                    if (stack.hasCustomHoverName()) {
                        golem.setCustomName(stack.getHoverName());
                    }
                }

                stack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
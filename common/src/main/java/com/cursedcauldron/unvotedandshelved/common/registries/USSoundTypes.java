package com.cursedcauldron.unvotedandshelved.common.registries;

import com.cursedcauldron.unvotedandshelved.common.resources.SoundTypeImpl;
import net.minecraft.world.level.block.SoundType;

public class USSoundTypes {
    public static final SoundType GLOW = new SoundTypeImpl(
            1.0F,
            2.0F,
            USSoundEvents.GLOWBERRY_DUST_PLACE,
            USSoundEvents.GLOWBERRY_DUST_STEP,
            USSoundEvents.GLOWBERRY_DUST_PLACE ,
            USSoundEvents.GLOWBERRY_DUST_PLACE,
            USSoundEvents.GLOWBERRY_DUST_PLACE
    );
}
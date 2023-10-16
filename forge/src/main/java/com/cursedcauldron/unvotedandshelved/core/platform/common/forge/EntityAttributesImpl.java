package com.cursedcauldron.unvotedandshelved.core.platform.common.forge;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;

import java.util.function.Supplier;

public class EntityAttributesImpl {
    public static Supplier<Attribute> getReachDistanceAttribute() {
        return ForgeMod.REACH_DISTANCE;
    }

    public static Supplier<Attribute> getAttackRangeAttribute() {
        return ForgeMod.ATTACK_RANGE;
    }
}

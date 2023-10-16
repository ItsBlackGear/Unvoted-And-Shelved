package com.cursedcauldron.unvotedandshelved.core.platform.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class EntityAttributes {
    public static final Supplier<Attribute> REACH_DISTANCE = getReachDistanceAttribute();
    public static final Supplier<Attribute> ATTACK_RANGE = getAttackRangeAttribute();

    @ExpectPlatform
    public static Supplier<Attribute> getReachDistanceAttribute() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Supplier<Attribute> getAttackRangeAttribute() {
        throw new AssertionError();
    }
}
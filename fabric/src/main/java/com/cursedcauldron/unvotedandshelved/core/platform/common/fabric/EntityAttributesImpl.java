package com.cursedcauldron.unvotedandshelved.core.platform.common.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class EntityAttributesImpl {
    public static Supplier<Attribute> getReachDistanceAttribute() {
        return () -> ReachEntityAttributes.REACH;
    }

    public static Supplier<Attribute> getAttackRangeAttribute() {
        return () -> ReachEntityAttributes.ATTACK_RANGE;
    }
}

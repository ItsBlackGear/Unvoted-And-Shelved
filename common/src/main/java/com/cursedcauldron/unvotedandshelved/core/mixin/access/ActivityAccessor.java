package com.cursedcauldron.unvotedandshelved.core.mixin.access;

import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Activity.class)
public interface ActivityAccessor {
    @Invoker("<init>")
    static Activity create(String string) {
        throw new UnsupportedOperationException();
    }
}

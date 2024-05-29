package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import com.blackgear.platform.core.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.mixin.access.ActivityAccessor;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.schedule.Activity;

import java.util.function.Supplier;

public class USActivities {
    public static final CoreRegistry<Activity> ACTIVITIES = CoreRegistry.create(Registry.ACTIVITY, UnvotedAndShelved.MOD_ID);

    public static final Supplier<Activity> TRACK_DARKNESS = create("track_darkness");
    public static final Supplier<Activity> INTERACT = create("interact");
    public static final Supplier<Activity> SPIN_HEAD = create("spin_head");

    public static Supplier<Activity> create(String key) {
        return ACTIVITIES.register(key, () -> ActivityAccessor.create(key));
    }
}
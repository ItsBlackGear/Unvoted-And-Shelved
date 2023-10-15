package com.cursedcauldron.unvotedandshelved.common.entity;

import net.minecraft.world.entity.Pose;

public enum USPoses {
    HEAD_SPIN,
    INTERACT,
    INTERACT_ABOVE,
    INTERACT_BELOW;

    public Pose get() {
        return Pose.valueOf(this.name());
    }
}
package com.cursedcauldron.unvotedandshelved.common.resources;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class SoundTypeImpl extends SoundType {
    private final Supplier<SoundEvent> breakSound;
    private final Supplier<SoundEvent> stepSound;
    private final Supplier<SoundEvent> placeSound;
    private final Supplier<SoundEvent> hitSound;
    private final Supplier<SoundEvent> fallSound;

    public SoundTypeImpl(Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
        this(1.0F, 1.0F, breakSound, stepSound, placeSound, hitSound, fallSound);
    }

    @SuppressWarnings("DataFlowIssue")
    public SoundTypeImpl(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
        super(volume, pitch, null, null, null, null, null);
        this.breakSound = breakSound;
        this.stepSound = stepSound;
        this.placeSound = placeSound;
        this.hitSound = hitSound;
        this.fallSound = fallSound;
    }

    @Override
    public SoundEvent getBreakSound() {
        return this.breakSound.get();
    }

    @Override
    public SoundEvent getStepSound() {
        return this.stepSound.get();
    }

    @Override
    public SoundEvent getPlaceSound() {
        return this.placeSound.get();
    }

    @Override
    public SoundEvent getHitSound() {
        return this.hitSound.get();
    }

    @Override
    public SoundEvent getFallSound() {
        return this.fallSound.get();
    }
}
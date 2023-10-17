package com.cursedcauldron.unvotedandshelved.common.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class USSoundEvents {
    public static final CoreRegistry<SoundEvent> SOUNDS = CoreRegistry.create(Registry.SOUND_EVENT, UnvotedAndShelved.MOD_ID);

    public static final Supplier<SoundEvent> GLOWBERRY_DUST_STEP = create("glowberry_dust_step");
    public static final Supplier<SoundEvent> GLOWBERRY_DUST_PLACE = create("glowberry_dust_place");
    public static final Supplier<SoundEvent> GLOWBERRY_DUST_COLLECT = create("glowberry_dust_collect");
    public static final Supplier<SoundEvent> COPPER_CLICK = create("copper_button_click");
    public static final Supplier<SoundEvent> HEAD_SPIN = create("copper_golem_headspin");
    public static final Supplier<SoundEvent> HEAD_SPIN_SLOWER = create("copper_golem_headspin_slower");
    public static final Supplier<SoundEvent> HEAD_SPIN_SLOWEST = create("copper_golem_headspin_slowest");
    public static final Supplier<SoundEvent> COPPER_GOLEM_WALK = create("copper_golem_walk");
    public static final Supplier<SoundEvent> COPPER_GOLEM_HIT = create("copper_golem_hit");
    public static final Supplier<SoundEvent> COPPER_GOLEM_DEATH = create("copper_golem_death");
    public static final Supplier<SoundEvent> COPPER_GOLEM_REPAIR = create("copper_golem_repair");
    public static final Supplier<SoundEvent> GLARE_GRUMPY_IDLE = create("glare_grumpy_idle");
    public static final Supplier<SoundEvent> GLARE_IDLE = create("glare_idle");
    public static final Supplier<SoundEvent> GLARE_HURT = create("glare_hurt");
    public static final Supplier<SoundEvent> GLARE_DEATH = create("glare_death");
    public static final Supplier<SoundEvent> GLARE_GIVE_GLOWBERRIES = create("glare_give_glow_berries");

    public static Supplier<SoundEvent> create(String key) {
        return SOUNDS.register(key, () -> new SoundEvent(new ResourceLocation(UnvotedAndShelved.MOD_ID, key)));
    }
}
package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.OxidizedCopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class USEntities {
    public static final CoreRegistry<EntityType<?>> ENTITIES = CoreRegistry.create(Registry.ENTITY_TYPE, UnvotedAndShelved.MOD_ID);

    public static final Supplier<EntityType<Glare>> GLARE = create("glare", EntityType.Builder.of(Glare::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(0.8F, 1.2F).clientTrackingRange(8));
    public static final Supplier<EntityType<CopperGolem>> COPPER_GOLEM = create("copper_golem", EntityType.Builder.of(CopperGolem::new, MobCategory.MISC).sized(0.85F, 1.2F).clientTrackingRange(8));
    public static final Supplier<EntityType<OxidizedCopperGolem>> OXIDIZED_COPPER_GOLEM = create("oxidized_copper_golem", EntityType.Builder.of(OxidizedCopperGolem::new, MobCategory.MISC).sized(0.85F, 1.2F).clientTrackingRange(8));

    public static <T extends Entity> Supplier<EntityType<T>> create(String key, EntityType.Builder<T> type) {
        return ENTITIES.register(key, () -> type.build(key));
    }
}
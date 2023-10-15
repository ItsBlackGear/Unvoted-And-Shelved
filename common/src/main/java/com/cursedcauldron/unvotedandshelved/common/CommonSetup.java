package com.cursedcauldron.unvotedandshelved.common;

import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.OxidizedCopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.cursedcauldron.unvotedandshelved.core.platform.common.BiomeManager;
import com.cursedcauldron.unvotedandshelved.core.platform.common.EntityRegistry;

public class CommonSetup {
    public static void onInstance() {
        EntityRegistry.attributes(USEntities.GLARE, Glare::createAttributes);
        EntityRegistry.attributes(USEntities.COPPER_GOLEM, CopperGolem::createAttributes);
        EntityRegistry.attributes(USEntities.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolem::createAttributes);
    }

    public static void postInstance() {
        BiomeManager.bootstrap();
    }
}
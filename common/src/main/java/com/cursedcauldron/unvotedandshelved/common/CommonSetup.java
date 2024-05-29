package com.cursedcauldron.unvotedandshelved.common;

import com.blackgear.platform.common.entity.EntityHandler;
import com.blackgear.platform.core.ParallelDispatch;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.OxidizedCopperGolem;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.cursedcauldron.unvotedandshelved.core.registry.ModCreativeTabs;

public class CommonSetup {
    public static void onInstance() {
        EntityHandler.addAttributes(USEntities.GLARE, Glare::createAttributes);
        EntityHandler.addAttributes(USEntities.COPPER_GOLEM, CopperGolem::createAttributes);
        EntityHandler.addAttributes(USEntities.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolem::createAttributes);
    }

    public static void postInstance(ParallelDispatch dispatch) {
        ModCreativeTabs.bootstrap();
    }
}
package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.ClientSetup;
import com.cursedcauldron.unvotedandshelved.client.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.common.CommonSetup;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.USItems;
import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.*;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USEntityTypeTags;
import com.cursedcauldron.unvotedandshelved.core.platform.ModInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnvotedAndShelved {
    public static final String MOD_ID = "unvotedandshelved";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onInstance).postCommon(CommonSetup::postInstance).client(ClientSetup::onInstance).postClient(ClientSetup::postInstance).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();

        USItems.ITEMS.register();
        USBlocks.BLOCKS.register();
        USParticles.PARTICLES.register();
        USSoundEvents.SOUNDS.register();

        // ===== ENTITIES =========================
        USActivities.ACTIVITIES.register();
        USDataSerializers.bootstrap();
        USEntities.ENTITIES.register();
        USMemoryModules.MEMORY_MODULES.register();
        USSensors.SENSORS.register();

        // ===== TAGS =============================
        USEntityTypeTags.TAGS.register();
    }
}
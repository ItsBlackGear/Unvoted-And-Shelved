package com.cursedcauldron.unvotedandshelved.core;

import com.blackgear.platform.core.ModInstance;
import com.cursedcauldron.unvotedandshelved.client.ClientSetup;
import com.cursedcauldron.unvotedandshelved.client.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.common.CommonSetup;
import com.cursedcauldron.unvotedandshelved.common.integration.USVanillaIntegration;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.USItems;
import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.*;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USEntityTypeTags;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USItemTags;
import com.cursedcauldron.unvotedandshelved.core.registry.ModEntityDataSerializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnvotedAndShelved {
    public static final String MOD_ID = "unvotedandshelved";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
        .client(ClientSetup::onInstance)
        .postClient(ClientSetup::postInstance)
        .common(CommonSetup::onInstance)
        .postCommon(CommonSetup::postInstance)
        .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();

        // ===== TAGS =============================
        USBlockTags.TAGS.register();
        USEntityTypeTags.TAGS.register();
        USItemTags.TAGS.register();

        // ===== REGISTRIES========================

        USItems.ITEMS.register();
        USBlocks.BLOCKS.register();
        USParticles.PARTICLES.register();
        USSoundEvents.SOUNDS.register();

        // ===== ENTITIES =========================
        USActivities.ACTIVITIES.register();
        ModEntityDataSerializers.SERIALIZERS.register();
        USEntities.ENTITIES.register();
        USMemoryModules.MEMORY_MODULES.register();
        USSensors.SENSORS.register();
        
        USVanillaIntegration.bootstrap();
    }
}
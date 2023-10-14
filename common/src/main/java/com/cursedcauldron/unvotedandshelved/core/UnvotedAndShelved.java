package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.ClientSetup;
import com.cursedcauldron.unvotedandshelved.common.CommonSetup;
import com.cursedcauldron.unvotedandshelved.core.platform.ModInstance;

public class UnvotedAndShelved {
    public static final String MOD_ID = "unvotedandshelved";
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onInstance).postCommon(CommonSetup::postInstance).client(ClientSetup::onInstance).postClient(ClientSetup::postInstance).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
    }
}
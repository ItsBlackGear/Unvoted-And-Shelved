package com.groupid.examplemod.core;

import com.groupid.examplemod.client.ClientSetup;
import com.groupid.examplemod.common.CommonSetup;
import com.groupid.examplemod.core.platform.ModInstance;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onInstance).postCommon(CommonSetup::postInstance).client(ClientSetup::onInstance).postClient(ClientSetup::postInstance).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
    }
}
package com.cursedcauldron.unvotedandshelved.common;

import com.cursedcauldron.unvotedandshelved.core.platform.common.BiomeManager;

public class CommonSetup {
    public static void onInstance() {

    }

    public static void postInstance() {
        BiomeManager.bootstrap();
    }
}
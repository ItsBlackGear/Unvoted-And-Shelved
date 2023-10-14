package com.groupid.examplemod.common;

import com.groupid.examplemod.core.platform.common.BiomeManager;

public class CommonSetup {
    public static void onInstance() {

    }

    public static void postInstance() {
        BiomeManager.bootstrap();
    }
}
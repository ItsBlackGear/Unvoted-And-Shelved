package com.groupid.examplemod.core.fabric;

import com.groupid.examplemod.core.ExampleMod;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.bootstrap();
    }
}
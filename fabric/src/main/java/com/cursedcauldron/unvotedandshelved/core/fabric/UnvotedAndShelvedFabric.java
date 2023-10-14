package com.cursedcauldron.unvotedandshelved.core.fabric;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.ModInitializer;

public class UnvotedAndShelvedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UnvotedAndShelved.bootstrap();
    }
}
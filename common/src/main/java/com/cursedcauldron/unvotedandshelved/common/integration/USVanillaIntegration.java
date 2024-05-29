package com.cursedcauldron.unvotedandshelved.common.integration;

import com.blackgear.platform.common.IntegrationHandler;
import com.cursedcauldron.unvotedandshelved.common.integration.interaction.CopperWaxInteraction;

public class USVanillaIntegration {
    public static void bootstrap() {
        IntegrationHandler.addInteraction(new CopperWaxInteraction());
    }
}
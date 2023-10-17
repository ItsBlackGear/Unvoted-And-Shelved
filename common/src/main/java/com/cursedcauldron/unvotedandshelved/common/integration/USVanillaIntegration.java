package com.cursedcauldron.unvotedandshelved.common.integration;

import com.cursedcauldron.unvotedandshelved.common.integration.interaction.CopperWaxInteraction;
import com.cursedcauldron.unvotedandshelved.core.platform.common.IntegrationRegistry;

public class USVanillaIntegration {
    public static void bootstrap() {
        IntegrationRegistry.interaction(
                new CopperWaxInteraction()
        );
    }
}
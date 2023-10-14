package com.cursedcauldron.unvotedandshelved.core.platform.common.fabric;

import com.cursedcauldron.unvotedandshelved.core.platform.common.IntegrationRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.item.context.UseOnContext;

public class IntegrationRegistryImpl {
    public static void interaction(IntegrationRegistry.Interaction interaction) {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> interaction.of(new UseOnContext(player, hand, hitResult)));
    }
}

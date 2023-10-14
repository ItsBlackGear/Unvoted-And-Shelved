package com.groupid.examplemod.core.platform.common.fabric;

import com.groupid.examplemod.core.platform.common.IntegrationRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.item.context.UseOnContext;

public class IntegrationRegistryImpl {
    public static void interaction(IntegrationRegistry.Interaction interaction) {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> interaction.of(new UseOnContext(player, hand, hitResult)));
    }
}

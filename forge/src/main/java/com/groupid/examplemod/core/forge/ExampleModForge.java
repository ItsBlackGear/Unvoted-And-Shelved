package com.groupid.examplemod.core.forge;

import com.groupid.examplemod.core.ExampleMod;
import com.groupid.examplemod.core.platform.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod(ExampleMod.MOD_ID)
public class ExampleModForge {
    public ExampleModForge() {
        ExampleMod.bootstrap();
        BiomeManager.bootstrap();
    }
}
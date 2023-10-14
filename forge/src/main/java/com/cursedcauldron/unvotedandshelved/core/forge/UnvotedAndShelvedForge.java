package com.cursedcauldron.unvotedandshelved.core.forge;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod(UnvotedAndShelved.MOD_ID)
public class UnvotedAndShelvedForge {
    public UnvotedAndShelvedForge() {
        UnvotedAndShelved.bootstrap();
        BiomeManager.bootstrap();
    }
}
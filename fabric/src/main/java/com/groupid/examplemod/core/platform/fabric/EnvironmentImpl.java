package com.groupid.examplemod.core.platform.fabric;

import com.groupid.examplemod.core.platform.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

public class EnvironmentImpl {
    public static CreativeModeTab createTab(ResourceLocation location, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(location, icon);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static WoodType create(ResourceLocation location) {
        WoodType type = WoodType.register(new WoodTypeBuilder(location));
        if (Environment.isClientSide()) Sheets.SIGN_MATERIALS.put(type, Sheets.createSignMaterial(type));
        return type;
    }

    public static class WoodTypeBuilder extends WoodType {
        private final ResourceLocation location;

        protected WoodTypeBuilder(ResourceLocation location) {
            super(location.getPath());
            this.location = location;
        }

        public ResourceLocation getLocation() {
            return this.location;
        }
    }
}
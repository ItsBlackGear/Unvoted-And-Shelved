package com.groupid.examplemod.core.platform.forge;

import com.groupid.examplemod.core.platform.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public class EnvironmentImpl {
    public static CreativeModeTab createTab(ResourceLocation location, Supplier<ItemStack> icon) {
        return new CreativeModeTab(location.toString().replace(":", ".")) {
            @Override public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isClientSide() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    public static WoodType create(ResourceLocation location) {
        WoodType type = WoodType.register(WoodType.create(location.toString()));
        if (Environment.isClientSide()) Sheets.addWoodType(type);
        return type;
    }
}
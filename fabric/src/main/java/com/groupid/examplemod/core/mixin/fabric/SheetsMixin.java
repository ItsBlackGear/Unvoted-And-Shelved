package com.groupid.examplemod.core.mixin.fabric;

import com.groupid.examplemod.core.platform.fabric.EnvironmentImpl;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheets.class)
public class SheetsMixin {
    @Inject(method = "createSignMaterial", at = @At("HEAD"), cancellable = true)
    private static void onCreateSignMaterial(WoodType type, CallbackInfoReturnable<Material> cir) {
        if (type instanceof EnvironmentImpl.WoodTypeBuilder builder) {
            cir.setReturnValue(new Material(Sheets.SIGN_SHEET, new ResourceLocation(builder.getLocation().getNamespace(), "entity/signs/" + builder.getLocation().getPath())));
        }
    }
}
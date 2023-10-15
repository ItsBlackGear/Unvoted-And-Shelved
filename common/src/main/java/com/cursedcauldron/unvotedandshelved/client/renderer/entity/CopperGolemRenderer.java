package com.cursedcauldron.unvotedandshelved.client.renderer.entity;

import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel<CopperGolem>> {
    private static final Map<WeatheringCopper.WeatherState, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), states -> {
        for (WeatheringCopper.WeatherState stage : WeatheringCopper.WeatherState.values()) {
            states.put(stage, new ResourceLocation(UnvotedAndShelved.MOD_ID, String.format("textures/entity/copper_golem/%s_copper_golem.png", stage.name().toLowerCase())));
        }
    });

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(USModelLayers.COPPER_GOLEM)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolem entity) {
        return TEXTURES.get(entity.getWeatherState());
    }
}
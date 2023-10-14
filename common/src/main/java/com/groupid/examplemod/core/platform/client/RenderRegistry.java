package com.groupid.examplemod.core.platform.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class RenderRegistry {
    @ExpectPlatform
    public static void block(RenderType type, Block... blocks) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> void renderer(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> renderer) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void layerDefinition(ModelLayerLocation layer, Supplier<LayerDefinition> definition) {
        throw new AssertionError();
    }

    public static <T extends Entity> void entity(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> renderer, ModelLayerLocation layer, Supplier<LayerDefinition> definition) {
        renderer(type, renderer);
        layerDefinition(layer, definition);
    }

    @SafeVarargs @ExpectPlatform
    public static void itemColor(ItemColor color, Supplier<? extends ItemLike>... items) {
        throw new AssertionError();
    }

    @SafeVarargs @ExpectPlatform
    public static void blockColor(BlockColor color, Supplier<? extends Block>... blocks) {
        throw new AssertionError();
    }
}
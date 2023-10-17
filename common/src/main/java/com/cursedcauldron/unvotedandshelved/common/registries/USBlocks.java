package com.cursedcauldron.unvotedandshelved.common.registries;

import com.cursedcauldron.unvotedandshelved.common.block.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class USBlocks {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK, UnvotedAndShelved.MOD_ID);

    public static final Supplier<Block> GLOWBERRY_DUST = create("glowberry_dust", () -> new GlowberryDustBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0f, 3600000.8f).noLootTable().sound(USSoundTypes.GLOW).lightLevel(state -> 10)));

    private static Boolean ocelotOrParrot(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public static Supplier<Block> create(String id, Supplier<Block> block) {
        return BLOCKS.register(id, block);
    }

    public static Supplier<Block> create(String id, Supplier<Block> block, CreativeModeTab tab) {
        Supplier<Block> registry = USBlocks.create(id, block);
        USItems.ITEMS.register(id, () -> new BlockItem(registry.get(), new Item.Properties().tab(tab)));
        return registry;
    }
}
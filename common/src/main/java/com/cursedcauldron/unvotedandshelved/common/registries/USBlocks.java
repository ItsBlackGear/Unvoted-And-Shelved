package com.cursedcauldron.unvotedandshelved.common.registries;

import com.cursedcauldron.unvotedandshelved.common.block.*;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class USBlocks {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK, UnvotedAndShelved.MOD_ID);

    // ========== GLARE BLOCKS =========================================================================================

    public static final Supplier<Block> GLOWBERRY_DUST = create(
            "glowberry_dust",
            () -> new GlowberryDustBlock(
                    Properties.of(Material.AIR).strength(-1.0f, 3600000.8f).noLootTable().sound(USSoundTypes.GLOW).lightLevel(state -> 10)
            )
    );

    // ========== COPPER BUTTONS =======================================================================================

    public static final Supplier<Block> COPPER_BUTTON = create(
            "copper_button",
            () -> new WeatheringCopperButtonBlock(
                    WeatherState.UNAFFECTED,
                    Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).sound(SoundType.COPPER)
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> EXPOSED_COPPER_BUTTON = create(
            "exposed_copper_button",
            () -> new WeatheringCopperButtonBlock(
                    WeatherState.EXPOSED,
                    Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(0.5F).sound(SoundType.COPPER)
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> WEATHERED_COPPER_BUTTON = create(
            "weathered_copper_button",
            () -> new WeatheringCopperButtonBlock(
                    WeatherState.WEATHERED,
                    Properties.of(Material.METAL, MaterialColor.WARPED_STEM).noCollission().strength(0.5F).sound(SoundType.COPPER)
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> OXIDIZED_COPPER_BUTTON = create(
            "oxidized_copper_button",
            () -> new WeatheringCopperButtonBlock(
                    WeatherState.OXIDIZED,
                    Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).noCollission().strength(0.5F).sound(SoundType.COPPER)
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> WAXED_COPPER_BUTTON = create(
            "waxed_copper_button",
            () -> new CopperButtonBlock(
                    WeatherState.UNAFFECTED,
                    Properties.copy(COPPER_BUTTON.get())
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_BUTTON = create(
            "waxed_exposed_copper_button",
            () -> new CopperButtonBlock(
                    WeatherState.EXPOSED,
                    Properties.copy(EXPOSED_COPPER_BUTTON.get())
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_BUTTON = create(
            "waxed_weathered_copper_button",
            () -> new CopperButtonBlock(
                    WeatherState.WEATHERED,
                    Properties.copy(WEATHERED_COPPER_BUTTON.get())
            ), CreativeModeTab.TAB_REDSTONE
    );
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_BUTTON = create(
            "waxed_oxidized_copper_button",
            () -> new CopperButtonBlock(
                    WeatherState.OXIDIZED,
                    Properties.copy(OXIDIZED_COPPER_BUTTON.get())
            ), CreativeModeTab.TAB_REDSTONE
    );

    // ========== COPPER PILLARS =======================================================================================

    public static final Supplier<Block> COPPER_PILLAR = create(
            "copper_pillar",
            () -> new WeatheringConnectedRotatedPillarBlock(
                    WeatherState.UNAFFECTED,
                    Properties.copy(Blocks.COPPER_BLOCK)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> EXPOSED_COPPER_PILLAR = create(
            "exposed_copper_pillar",
            () -> new WeatheringConnectedRotatedPillarBlock(
                    WeatherState.EXPOSED,
                    Properties.copy(Blocks.EXPOSED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> WEATHERED_COPPER_PILLAR = create(
            "weathered_copper_pillar",
            () -> new WeatheringConnectedRotatedPillarBlock(
                    WeatherState.WEATHERED,
                    Properties.copy(Blocks.WEATHERED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> OXIDIZED_COPPER_PILLAR = create(
            "oxidized_copper_pillar",
            () -> new WeatheringConnectedRotatedPillarBlock(
                    WeatherState.WEATHERED,
                    Properties.copy(Blocks.OXIDIZED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> WAXED_COPPER_PILLAR = create(
            "waxed_copper_pillar",
            () -> new ConnectedRotatedPillarBlock(
                    Properties.copy(Blocks.COPPER_BLOCK)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_PILLAR = create(
            "waxed_exposed_copper_pillar",
            () -> new ConnectedRotatedPillarBlock(
                    Properties.copy(Blocks.EXPOSED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_PILLAR = create(
            "waxed_weathered_copper_pillar",
            () -> new ConnectedRotatedPillarBlock(
                    Properties.copy(Blocks.WEATHERED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_PILLAR = create(
            "waxed_oxidized_copper_pillar",
            () -> new ConnectedRotatedPillarBlock(
                    Properties.copy(Blocks.OXIDIZED_COPPER)
            ), CreativeModeTab.TAB_BUILDING_BLOCKS
    );

    public static Supplier<Block> create(String id, Supplier<Block> block) {
        return BLOCKS.register(id, block);
    }

    public static Supplier<Block> create(String id, Supplier<Block> block, CreativeModeTab tab) {
        Supplier<Block> registry = USBlocks.create(id, block);
        USItems.ITEMS.register(id, () -> new BlockItem(registry.get(), new Item.Properties().tab(tab)));
        return registry;
    }
}
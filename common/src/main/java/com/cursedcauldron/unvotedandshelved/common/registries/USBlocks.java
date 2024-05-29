package com.cursedcauldron.unvotedandshelved.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.block.*;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class USBlocks {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK, UnvotedAndShelved.MOD_ID);

    // ========== GLARE BLOCKS =========================================================================================

    public static final Supplier<Block> GLOWBERRY_DUST = register(
        "glowberry_dust",
        () -> new GlowberryDustBlock(
            Properties.of(Material.AIR)
                .strength(-1.0f, 3600000.8f)
                .noLootTable()
                .sound(USSoundTypes.GLOW)
                .lightLevel(state -> 10)
        ));

    // ========== COPPER BUTTONS =======================================================================================

    public static final Supplier<Block> COPPER_BUTTON = register(
        "copper_button",
        () -> new WeatheringCopperButtonBlock(
            WeatherState.UNAFFECTED,
            Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE)
                .noCollission()
                .strength(0.5F)
                .sound(SoundType.COPPER)
        ));
    public static final Supplier<Block> EXPOSED_COPPER_BUTTON = register(
        "exposed_copper_button",
        () -> new WeatheringCopperButtonBlock(
            WeatherState.EXPOSED,
            Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY)
                .noCollission()
                .strength(0.5F)
                .sound(SoundType.COPPER)
        ));
    public static final Supplier<Block> WEATHERED_COPPER_BUTTON = register(
        "weathered_copper_button",
        () -> new WeatheringCopperButtonBlock(
            WeatherState.WEATHERED,
            Properties.of(Material.METAL, MaterialColor.WARPED_STEM)
                .noCollission()
                .strength(0.5F)
                .sound(SoundType.COPPER)
        ));
    public static final Supplier<Block> OXIDIZED_COPPER_BUTTON = register(
        "oxidized_copper_button",
        () -> new WeatheringCopperButtonBlock(
            WeatherState.OXIDIZED,
            Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM)
                .noCollission()
                .strength(0.5F)
                .sound(SoundType.COPPER)
        ));
    public static final Supplier<Block> WAXED_COPPER_BUTTON = register(
        "waxed_copper_button",
        () -> new CopperButtonBlock(
            WeatherState.UNAFFECTED,
            Properties.copy(COPPER_BUTTON.get())
        ));
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_BUTTON = register(
        "waxed_exposed_copper_button",
        () -> new CopperButtonBlock(
            WeatherState.EXPOSED,
            Properties.copy(EXPOSED_COPPER_BUTTON.get())
        ));
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_BUTTON = register(
        "waxed_weathered_copper_button",
        () -> new CopperButtonBlock(
            WeatherState.WEATHERED,
            Properties.copy(WEATHERED_COPPER_BUTTON.get())
        ));
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_BUTTON = register(
        "waxed_oxidized_copper_button",
        () -> new CopperButtonBlock(
            WeatherState.OXIDIZED,
            Properties.copy(OXIDIZED_COPPER_BUTTON.get())
        ));

    // ========== COPPER PILLARS =======================================================================================

    public static final Supplier<Block> COPPER_PILLAR = register(
        "copper_pillar",
        () -> new WeatheringConnectedRotatedPillarBlock(
            WeatherState.UNAFFECTED,
            Properties.copy(Blocks.COPPER_BLOCK)
        ));
    public static final Supplier<Block> EXPOSED_COPPER_PILLAR = register(
        "exposed_copper_pillar",
        () -> new WeatheringConnectedRotatedPillarBlock(
            WeatherState.EXPOSED,
            Properties.copy(Blocks.EXPOSED_COPPER)
        ));
    public static final Supplier<Block> WEATHERED_COPPER_PILLAR = register(
        "weathered_copper_pillar",
        () -> new WeatheringConnectedRotatedPillarBlock(
            WeatherState.WEATHERED,
            Properties.copy(Blocks.WEATHERED_COPPER)
        ));
    public static final Supplier<Block> OXIDIZED_COPPER_PILLAR = register(
        "oxidized_copper_pillar",
        () -> new WeatheringConnectedRotatedPillarBlock(
            WeatherState.WEATHERED,
            Properties.copy(Blocks.OXIDIZED_COPPER)
        ));
    public static final Supplier<Block> WAXED_COPPER_PILLAR = register(
        "waxed_copper_pillar",
        () -> new ConnectedRotatedPillarBlock(
            Properties.copy(Blocks.COPPER_BLOCK)
        ));
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_PILLAR = register(
        "waxed_exposed_copper_pillar",
        () -> new ConnectedRotatedPillarBlock(
            Properties.copy(Blocks.EXPOSED_COPPER)
        ));
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_PILLAR = register(
        "waxed_weathered_copper_pillar",
        () -> new ConnectedRotatedPillarBlock(
            Properties.copy(Blocks.WEATHERED_COPPER)
        ));
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_PILLAR = register(
        "waxed_oxidized_copper_pillar",
        () -> new ConnectedRotatedPillarBlock(
            Properties.copy(Blocks.OXIDIZED_COPPER)
        ));
    
    private static <T extends Block> Supplier<T> register(String key, Supplier<T> block) {
        return register(key, block, entry -> new BlockItem(entry.get(), new Item.Properties()));
    }
    
    private static <T extends Block> Supplier<T> register(String key, Supplier<T> block, Function<Supplier<T>, Item> item) {
        Supplier<T> entry = BLOCKS.register(key, block);
        USItems.ITEMS.register(key, () -> item.apply(entry));
        return entry;
    }
}
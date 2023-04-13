/*
 * Copyright (c) 2022-2023 Maxmani and contributors.
 * Licensed under the EUPL-1.2 or later.
 */

package net.reimaden.arcadiandream.datagen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.reimaden.arcadiandream.ArcadianDream;
import net.reimaden.arcadiandream.block.ModBlocks;
import net.reimaden.arcadiandream.item.ModItems;
import net.reimaden.arcadiandream.util.ModTags;
import net.reimaden.arcadiandream.util.client.ModModels;

import java.util.function.Consumer;

public class ModModelGenerator extends FabricModelProvider {

    private static final ImmutableList<Block> SIMPLE_CUBES = ImmutableList.of(
            ModBlocks.DRAGON_GEM_ORE, ModBlocks.DEEPSLATE_DRAGON_GEM_ORE, ModBlocks.END_STONE_DRAGON_GEM_ORE, ModBlocks.MAKAITE_BLOCK,
            ModBlocks.RAW_MAKAITE_BLOCK, ModBlocks.MAKAITE_ORE
    );

    private static final ImmutableList<Item> GENERATED_ITEMS = ImmutableList.of(
            ModItems.POWER_ITEM, ModItems.BIG_POWER_ITEM, ModItems.POINT_ITEM, ModItems.MAX_POINT_ITEM,
            ModItems.BOMB_ITEM, ModItems.EXTEND_ITEM, ModItems.STAR_ITEM, ModItems.DRAGON_GEM,
            ModItems.HEAVENLY_PEACH, ModItems.IBUKI_GOURD, ModItems.MAKAITE_BOOTS, ModItems.MAKAITE_CHESTPLATE,
            ModItems.MAKAITE_HELMET, ModItems.MAKAITE_LEGGINGS, ModItems.MAKAITE_INGOT, ModItems.MUSIC_DISC_FAIRY_PLAYGROUND,
            ModItems.MUSIC_DISC_THE_SHRINE_LONG_FORGOTTEN, ModItems.ORDINARY_HAT, ModItems.RAW_MAKAITE, ModItems.HEALING_CHARM,
            ModItems.HOURAI_ELIXIR, ModItems.FAITH_ITEM, ModItems.SPREAD_PATTERN, ModItems.RAY_PATTERN,
            ModItems.RING_PATTERN, ModItems.CONE_PATTERN, ModItems.DOUBLE_PATTERN, ModItems.TRIPLE_PATTERN
    );

    private static final ImmutableList<Item> HANDHELD_ITEMS = ImmutableList.of(
            ModItems.HISOU_SWORD, ModItems.MAKAITE_AXE, ModItems.MAKAITE_HOE, ModItems.MAKAITE_PICKAXE,
            ModItems.MAKAITE_SHOVEL, ModItems.MAKAITE_SWORD, ModItems.MIRACLE_MALLET
    );

    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator block) {
        for (int i = 0; i < SIMPLE_CUBES.size(); i++) {
            block.registerSimpleCubeAll(SIMPLE_CUBES.get(i));
        }
        block.registerSouthDefaultHorizontalFacing(TexturedModel.TEMPLATE_GLAZED_TERRACOTTA, ModBlocks.DRAGON_GEM_BLOCK);
        block.registerNorthDefaultHorizontalRotation(ModBlocks.RITUAL_SHRINE);
        block.registerSimpleState(ModBlocks.ONBASHIRA);
        block.registerSimpleState(ModBlocks.ONBASHIRA_PILLAR);
        block.registerSingleton(ModBlocks.DANMAKU_CRAFTING_TABLE, TexturedModel.CUBE_BOTTOM_TOP);
        registerSeal(block);
    }

    @SuppressWarnings("SameParameterValue")
    private void registerNorthDefaultRotationStates(Block block, Consumer<BlockStateSupplier> blockStateCollector) {
        blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(block))).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()));
    }

    private void registerSeal(BlockStateModelGenerator generator) {
        registerNorthDefaultRotationStates(ModBlocks.MYSTERIOUS_SEAL, generator.blockStateCollector);
        generator.registerItemModel(ModBlocks.MYSTERIOUS_SEAL);
    }

    @Override
    public void generateItemModels(ItemModelGenerator item) {
        for (int i = 0; i < GENERATED_ITEMS.size(); i++) {
            item.register(GENERATED_ITEMS.get(i), Models.GENERATED);
        }
        for (int i = 0; i < HANDHELD_ITEMS.size(); i++) {
            item.register(HANDHELD_ITEMS.get(i), Models.HANDHELD);
        }
        for (int i = 0; i < 11; i++) {
            item.register(ModItems.MOCHI_MALLET, "_" + i, ModModels.HANDHELD_BIG);
        }

        item.register(ModItems.DEATH_SCYTHE, ModModels.HANDHELD_BIG);

        for (Item core : ModTags.BULLET_CORES) {
            Identifier itemId = Registries.ITEM.getId(core);
            String bulletCoreType = itemId.getPath().split("_")[0];
            String textureName = "item/" + bulletCoreType + "_shot";
            registerWithSpecificTexture(core, textureName, ModModels.DANMAKU, item);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void registerWithSpecificTexture(Item item, String name, Model model, ItemModelGenerator generator) {
        model.upload(ModelIds.getItemModelId(item), TextureMap.layer0(new Identifier(ArcadianDream.MOD_ID, name)), generator.writer);
    }
}

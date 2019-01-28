package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.blocks.*;
import com.aranaira.arcanearchives.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

public class BlockLibrary
{

	public static final List<Block> BLOCKS = new ArrayList<Block>();

	//Matrices
	public static final Block MATRIX_CRYSTAL_CORE = new MatrixCrystalCore();
	public static final Block MATRIX_REPOSITORY = new MatrixRepository();
	public static final Block MATRIX_RESERVOIR = new MatrixReservoir();
	public static final Block MATRIX_STORAGE = new MatrixStorage();
	public static final Block MATRIX_DISTILLATE = new MatrixDistillate(); //TODO: Check if Thaumcraft is loaded 

	//Blocks
	public static final Block STORAGE_RAW_QUARTZ = new StorageRawQuartz();
	public static final Block STORAGE_CUT_QUARTZ = new StorageCutQuartz();
	public static final Block RADIANT_CHEST = new RadiantChest();
	public static final Block RADIANT_CRAFTING_TABLE = new RadiantCraftingTable();
	public static final Block RADIANT_LANTERN = new RadiantLantern();
	public static final Block RADIANT_RESONATOR = new RadiantResonator();
	public static final Block RAW_QUARTZ = new RawQuartz();
	public static final Block DOMINION_CRYSTAL = new DominionCrystal();
	public static final Block GEMCUTTERS_TABLE = new GemcuttersTable();
	//Fix for dynamic material base on block its extending.
	public static final Block ACCESSOR = new AccessorBlock(Material.GLASS);

	// Tile Entities. TODO: Don't forget to update the RegistryHandler when adding new ones.
	public static final TileEntity RADIANT_RESONATOR_TILE_ENTITY = new RadiantResonatorTileEntity();
	public static final TileEntity MATRIX_CORE_TILE_ENTITY = new MatrixCoreTileEntity();
	public static final TileEntity MATRIX_REPOSITORY_TILE_ENTITY = new MatrixRepositoryTileEntity();
	public static final TileEntity ACCESSOR_TILE_ENTITY = new AccessorTileEntity();
	public static final TileEntity RADIANT_CHEST_TILE_ENTITY = new RadiantChestTileEntity();
	public static final TileEntity GEMCUTTERS_TABLE_TILE_ENTITY = new GemcuttersTableTileEntity();
	public static final TileEntity RADIANT_CRAFTING_TABLE_TILE_ENTITY = new RadiantCraftingTableTileEntity();
	public static final TileEntity MATRIX_STORAGE_TILE_ENTITY = new MatrixStorageTileEntity();
}

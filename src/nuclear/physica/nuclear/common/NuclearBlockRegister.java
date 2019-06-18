package physica.nuclear.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import physica.api.core.IContent;
import physica.library.item.ItemBlockMetadata;
import physica.nuclear.NuclearReferences;
import physica.nuclear.common.block.BlockCentrifuge;
import physica.nuclear.common.block.BlockChemicalBoiler;
import physica.nuclear.common.block.BlockChemicalExtractor;
import physica.nuclear.common.block.BlockControlRod;
import physica.nuclear.common.block.BlockElectromagnet;
import physica.nuclear.common.block.BlockFissionReactor;
import physica.nuclear.common.block.BlockFusionReactor;
import physica.nuclear.common.block.BlockInsertableControlRod;
import physica.nuclear.common.block.BlockMeltedReactor;
import physica.nuclear.common.block.BlockNeutronCaptureChamber;
import physica.nuclear.common.block.BlockParticleAccelerator;
import physica.nuclear.common.block.BlockPlasma;
import physica.nuclear.common.block.BlockQuantumAssembler;
import physica.nuclear.common.block.BlockRadioactiveGrass;
import physica.nuclear.common.block.BlockSiren;
import physica.nuclear.common.block.BlockThermometer;
import physica.nuclear.common.block.BlockTurbine;
import physica.nuclear.common.block.BlockUraniumOre;
import physica.nuclear.common.tile.TileCentrifuge;
import physica.nuclear.common.tile.TileChemicalBoiler;
import physica.nuclear.common.tile.TileChemicalExtractor;
import physica.nuclear.common.tile.TileFissionReactor;
import physica.nuclear.common.tile.TileFusionReactor;
import physica.nuclear.common.tile.TileInsertableControlRod;
import physica.nuclear.common.tile.TileMeltedReactor;
import physica.nuclear.common.tile.TileNeutronCaptureChamber;
import physica.nuclear.common.tile.TileParticleAccelerator;
import physica.nuclear.common.tile.TilePlasma;
import physica.nuclear.common.tile.TileQuantumAssembler;
import physica.nuclear.common.tile.TileSiren;
import physica.nuclear.common.tile.TileTurbine;

public class NuclearBlockRegister implements IContent {

	public static BlockParticleAccelerator blockParticleAccelerator;
	public static BlockQuantumAssembler blockQuantumAssembler;
	public static BlockCentrifuge blockCentrifuge;
	public static BlockChemicalBoiler blockChemicalBoiler;
	public static BlockChemicalExtractor blockChemicalExtractor;
	public static BlockFissionReactor blockFissionReactor;
	public static BlockNeutronCaptureChamber blockNeutronCaptureChamber;
	public static BlockFusionReactor blockFusionReactor;
	public static BlockTurbine blockTurbine;
	public static BlockSiren blockSiren;

	public static BlockElectromagnet blockElectromagnet;
	public static BlockThermometer blockThermometer;
	public static BlockControlRod blockControlRod;
	public static BlockUraniumOre blockUraniumOre;
	public static BlockPlasma blockPlasma;
	public static BlockInsertableControlRod blockInsertableControlRod;
	public static BlockMeltedReactor blockMeltedReactor;
	public static BlockRadioactiveGrass blockRadioactiveGrass;

	@Override
	public void preInit() {
		GameRegistry.registerBlock(blockParticleAccelerator = new BlockParticleAccelerator(), "accelerator");
		GameRegistry.registerTileEntity(TileParticleAccelerator.class, NuclearReferences.PREFIX + "accelerator");
		GameRegistry.registerBlock(blockQuantumAssembler = new BlockQuantumAssembler(), "assembler");
		GameRegistry.registerTileEntity(TileQuantumAssembler.class, NuclearReferences.PREFIX + "assembler");
		GameRegistry.registerBlock(blockCentrifuge = new BlockCentrifuge(), "centrifuge");
		GameRegistry.registerTileEntity(TileCentrifuge.class, NuclearReferences.PREFIX + "centrifuge");
		GameRegistry.registerBlock(blockChemicalBoiler = new BlockChemicalBoiler(), "chemicalBoiler");
		GameRegistry.registerTileEntity(TileChemicalBoiler.class, NuclearReferences.PREFIX + "chemicalBoiler");
		GameRegistry.registerBlock(blockChemicalExtractor = new BlockChemicalExtractor(), "chemicalExtractor");
		GameRegistry.registerTileEntity(TileChemicalExtractor.class, NuclearReferences.PREFIX + "chemicalExtractor");
		GameRegistry.registerBlock(blockFissionReactor = new BlockFissionReactor(), "fissionReactor");
		GameRegistry.registerTileEntity(TileFissionReactor.class, NuclearReferences.PREFIX + "fissionReactor");
		GameRegistry.registerBlock(blockNeutronCaptureChamber = new BlockNeutronCaptureChamber(), "neutronCaptureChamber");
		GameRegistry.registerTileEntity(TileNeutronCaptureChamber.class, NuclearReferences.PREFIX + "neutronCaptureChamber");
		GameRegistry.registerBlock(blockThermometer = new BlockThermometer(), "thermometer");
		GameRegistry.registerBlock(blockControlRod = new BlockControlRod(), "controlRod");
		GameRegistry.registerBlock(blockUraniumOre = new BlockUraniumOre(), "uraniumOre");
		GameRegistry.registerBlock(blockFusionReactor = new BlockFusionReactor(), "fusionReactor");
		GameRegistry.registerTileEntity(TileFusionReactor.class, NuclearReferences.PREFIX + "fusionReactor");
		GameRegistry.registerBlock(blockTurbine = new BlockTurbine(), "turbine");
		GameRegistry.registerTileEntity(TileTurbine.class, NuclearReferences.PREFIX + "turbine");
		GameRegistry.registerBlock(blockElectromagnet = new BlockElectromagnet(), ItemBlockMetadata.class, "electromagnet");
		GameRegistry.registerBlock(blockPlasma = new BlockPlasma(), "plasma");
		GameRegistry.registerTileEntity(TilePlasma.class, NuclearReferences.PREFIX + "plasma");
		GameRegistry.registerBlock(blockSiren = new BlockSiren(), "siren");
		GameRegistry.registerTileEntity(TileSiren.class, NuclearReferences.PREFIX + "siren");

		GameRegistry.registerBlock(blockInsertableControlRod = new BlockInsertableControlRod(), "insertableControlRod");
		GameRegistry.registerTileEntity(TileInsertableControlRod.class, NuclearReferences.PREFIX + "insertableControlRod");

		GameRegistry.registerBlock(blockMeltedReactor = new BlockMeltedReactor(), "meltedReactor");
		GameRegistry.registerTileEntity(TileMeltedReactor.class, NuclearReferences.PREFIX + "meltedReactor");

		GameRegistry.registerBlock(blockRadioactiveGrass = new BlockRadioactiveGrass(), "radioactiveGrass");

		OreDictionary.registerOre("oreUranium", NuclearBlockRegister.blockUraniumOre);
	}
}

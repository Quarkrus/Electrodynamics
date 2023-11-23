package electrodynamics.registers;

import static electrodynamics.registers.UnifiedElectrodynamicsRegister.supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electrodynamics.api.ISubtype;
import electrodynamics.api.References;
import electrodynamics.common.block.BlockCustomGlass;
import electrodynamics.common.block.BlockFrame;
import electrodynamics.common.block.BlockLogisticalManager;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.BlockMultiSubnode;
import electrodynamics.common.block.BlockOre;
import electrodynamics.common.block.BlockSeismicMarker;
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.connect.BlockLogisticalWire;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ElectrodynamicsBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
	public static final HashMap<ISubtype, RegistryObject<Block>> SUBTYPEBLOCKREGISTER_MAPPINGS = new HashMap<>();

	public static BlockMultiSubnode multi;
	public static BlockSeismicMarker blockSeismicMarker;
	public static BlockFrame blockFrame;
	public static BlockFrame blockFrameCorner;
	public static BlockLogisticalManager blockLogisticalManager;

	static {
		for (SubtypeOre subtype : SubtypeOre.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockOre(subtype), subtype)));
		}
		for (SubtypeOreDeepslate subtype : SubtypeOreDeepslate.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockOre(subtype), subtype)));
		}
		for (SubtypeRawOreBlock subtype : SubtypeRawOreBlock.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new Block(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F)), subtype)));
		}
		for (SubtypeMachine subtype : SubtypeMachine.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockMachine(subtype), subtype)));
		}
		for (SubtypeWire subtype : SubtypeWire.values()) {
			if(subtype.wireClass == WireClass.LOGISTICAL) {
				SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockLogisticalWire(subtype), subtype)));
			} else {
				SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockWire(subtype), subtype)));
			}
			
		}
		for (SubtypeFluidPipe subtype : SubtypeFluidPipe.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockFluidPipe(subtype), subtype)));
		}
		for (SubtypeGlass subtype : SubtypeGlass.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new BlockCustomGlass(subtype), subtype)));
		}
		for (SubtypeResourceBlock subtype : SubtypeResourceBlock.values()) {
			SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(() -> new Block(Properties.of(subtype.getMaterial()).strength(subtype.getHardness(), subtype.getResistance()).sound(subtype.getSoundType())), subtype)));
		}
	}

	public static final RegistryObject<Block> MULTI_SUBNODE = BLOCKS.register("multisubnode", supplier(() -> multi = new BlockMultiSubnode()));
	public static final RegistryObject<Block> SEISMIC_MARKER = BLOCKS.register("seismicmarker", supplier(() -> blockSeismicMarker = new BlockSeismicMarker()));
	public static final RegistryObject<Block> FRAME = BLOCKS.register("frame", supplier(() -> blockFrame = new BlockFrame(0)));
	public static final RegistryObject<Block> FRAME_CORNER = BLOCKS.register("framecorner", supplier(() -> blockFrameCorner = new BlockFrame(1)));
	public static final RegistryObject<Block> LOGISTICAL_MANAGER = BLOCKS.register("logisticalmanager", supplier(() -> blockLogisticalManager = new BlockLogisticalManager()));

	public static Block[] getAllBlockForSubtype(ISubtype[] values) {
		List<Block> list = new ArrayList<>();
		for (ISubtype value : values) {
			list.add(SUBTYPEBLOCKREGISTER_MAPPINGS.get(value).get());
		}
		return list.toArray(new Block[] {});
	}

	public static Block getBlock(ISubtype value) {
		return SUBTYPEBLOCKREGISTER_MAPPINGS.get(value).get();
	}

}

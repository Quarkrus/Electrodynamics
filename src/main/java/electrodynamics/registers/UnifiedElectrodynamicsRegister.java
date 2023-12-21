package electrodynamics.registers;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

public class UnifiedElectrodynamicsRegister {

	public static void register(IEventBus bus) {
		ElectrodynamicsBlocks.BLOCKS.register(bus);
		ElectrodynamicsBlockTypes.BLOCK_ENTITY_TYPES.register(bus);
		ElectrodynamicsItems.ITEMS.register(bus);
		ElectrodynamicsFluids.FLUIDS.register(bus);
		ElectrodynamicsEntities.ENTITIES.register(bus);
		ElectrodynamicsFeatures.CONFIGURED_FEATURES.register(bus);
		ElectrodynamicsFeatures.PLACED_FEATURES.register(bus);
		ElectrodynamicsMenuTypes.MENU_TYPES.register(bus);
		ElectrodynamicsSounds.SOUNDS.register(bus);
		ElectrodynamicsRuleTestTypes.RULE_TEST_TYPES.register(bus);
	}

	static {
		// machines
		// cleaner and simpler is it not?
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnace), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacedouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacetriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremill), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilldouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilltriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusher), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusherdouble), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrushertriple), ElectroTextUtils.voltageTooltip(960));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinder), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinderdouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrindertriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.oxidationfurnace), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralwasher), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalmixer), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalcrystallizer), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.energizedalloyer), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.reinforcedalloyer), ElectroTextUtils.voltageTooltip(960));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.lathe), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerlv), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargermv), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerhv), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.fermentationplant), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricpump), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electrolyticseparator), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnace), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacedouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacetriple), ElectroTextUtils.voltageTooltip(480));
		// generators
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.solarpanel), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedsolarpanel), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.thermoelectricgenerator), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.combustionchamber), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.hydroelectricgenerator), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.windmill), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.coalgenerator), ElectroTextUtils.voltageTooltip(120));

		// misc
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.downgradetransformer), ElectroTextUtils.tooltip("transformer.energyloss"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.upgradetransformer), ElectroTextUtils.tooltip("transformer.energyloss"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.batterybox), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.lithiumbatterybox), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.carbynebatterybox), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativepowersource), ElectroTextUtils.tooltip("creativepowersource.joke"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativefluidsource), ElectroTextUtils.tooltip("creativefluidsource.joke"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.fluidvoid), ElectroTextUtils.tooltip("fluidvoid"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.tanksteel), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(8, DisplayUnit.BUCKETS)));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankreinforced), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(32, DisplayUnit.BUCKETS)));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankhsla), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(128, DisplayUnit.BUCKETS)));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.blockSeismicMarker, ElectroTextUtils.tooltip("seismicmarker.redstone"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.seismicrelay), ElectroTextUtils.tooltip("seismicrelay.use"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.coolantresavoir), ElectroTextUtils.tooltip("coolantresavoir.place"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.motorcomplex), ElectroTextUtils.tooltip("motorcomplex.use"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.blockFrame, ElectroTextUtils.tooltip("blockframe.joke"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.blockFrameCorner, ElectroTextUtils.tooltip("blockframe.joke"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.getBlock(SubtypeMachine.quarry), ElectroTextUtils.tooltip("quarry.power"));
		BlockItemDescriptable.addDescription(() -> ElectrodynamicsBlocks.blockLogisticalManager, ElectroTextUtils.tooltip("logisticalmanager.use"));

	}

	public static <T> Supplier<? extends T> supplier(Supplier<? extends T> entry) {
		return entry;
	}

	public static <T> Supplier<? extends T> supplier(Supplier<? extends T> entry, ISubtype en) {
		return entry;
	}

	public static Block getSafeBlock(ISubtype type) {
		return ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(type).get();
	}

}

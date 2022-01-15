package electrodynamics.common.tile;

import electrodynamics.DeferredRegisters;
import electrodynamics.SoundRegister;
import electrodynamics.api.electricity.CapabilityElectrodynamic;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.common.inventory.container.ContainerO2OProcessor;
import electrodynamics.common.inventory.container.ContainerO2OProcessorDouble;
import electrodynamics.common.inventory.container.ContainerO2OProcessorTriple;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.item2item.specificmachines.WireMillRecipe;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

public class TileWireMill extends GenericTile {

    private static int itemBiSize = 0;
    private static int inputBucketSlots = 0;
    private static int outputBucketSlots = 0;
    private static int upgradeSlots = 3;

    private static int inputPerProc = 1;

    private static int knownInvSize = inputBucketSlots + outputBucketSlots + upgradeSlots + itemBiSize;

    public TileWireMill(BlockPos worldPosition, BlockState blockState) {
	this(0, worldPosition, blockState);
    }

    public TileWireMill(int extra, BlockPos worldPosition, BlockState blockState) {
	super(extra == 1 ? DeferredRegisters.TILE_WIREMILLDOUBLE.get()
		: extra == 2 ? DeferredRegisters.TILE_WIREMILLTRIPLE.get() : DeferredRegisters.TILE_WIREMILL.get(), worldPosition, blockState);

	int processorCount = extra + 1;
	int inputCount = inputPerProc * (extra + 1);
	int outputCount = 1 * (extra + 1);
	int invSize = knownInvSize + inputCount + outputCount;

	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler());
	addComponent(new ComponentTickable().tickClient(this::tickClient));
	addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH)
		.voltage(CapabilityElectrodynamic.DEFAULT_VOLTAGE * Math.pow(2, extra)).joules(Constants.WIREMILL_USAGE_PER_TICK * 20 * (extra + 1)));

	int[] ints = new int[extra + 1];
	for (int i = 0; i <= extra; i++) {
	    ints[i] = i * 2;
	}

	addComponent(new ComponentInventory(this).size(invSize)
		.slotSizes(inputCount, outputCount, itemBiSize, upgradeSlots, inputBucketSlots, outputBucketSlots, processorCount, inputPerProc)
		.valid(getPredicateMulti(inputCount, outputCount, itemBiSize, inputBucketSlots + outputBucketSlots, upgradeSlots, invSize, ints))
		.setMachineSlots(extra).shouldSendInfo());
	addComponent(new ComponentContainerProvider("container.wiremill" + extra).createMenu((id, player) -> (extra == 0
		? new ContainerO2OProcessor(id, player, getComponent(ComponentType.Inventory), getCoordsArray())
		: extra == 1 ? new ContainerO2OProcessorDouble(id, player, getComponent(ComponentType.Inventory), getCoordsArray())
			: extra == 2 ? new ContainerO2OProcessorTriple(id, player, getComponent(ComponentType.Inventory), getCoordsArray()) : null)));

	for (int i = 0; i <= extra; i++) {
	    addProcessor(new ComponentProcessor(this).setProcessorNumber(i)
		    .canProcess(component -> component.canProcessItem2ItemRecipe(component, WireMillRecipe.class,
			    ElectrodynamicsRecipeInit.WIRE_MILL_TYPE))
		    .process(component -> component.processItem2ItemRecipe(component, WireMillRecipe.class))
		    .requiredTicks(Constants.WIREMILL_REQUIRED_TICKS).usage(Constants.WIREMILL_USAGE_PER_TICK));
	}
    }

    protected void tickClient(ComponentTickable tickable) {
	boolean has = getType() == DeferredRegisters.TILE_ELECTRICFURNACEDOUBLE.get()
		? getProcessor(0).operatingTicks + getProcessor(1).operatingTicks > 0
		: getType() == DeferredRegisters.TILE_ELECTRICFURNACETRIPLE.get()
			? getProcessor(0).operatingTicks + getProcessor(1).operatingTicks + getProcessor(2).operatingTicks > 0
			: getProcessor(0).operatingTicks > 0;
	if (has) {
	    if (level.random.nextDouble() < 0.15) {
		level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(),
			worldPosition.getY() + level.random.nextDouble() * 0.5 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D,
			0.0D);
	    }
	    if (tickable.getTicks() % 200 == 0) {
		SoundAPI.playSound(SoundRegister.SOUND_HUM.get(), SoundSource.BLOCKS, 1, 1, worldPosition);
	    }
	}
    }

}

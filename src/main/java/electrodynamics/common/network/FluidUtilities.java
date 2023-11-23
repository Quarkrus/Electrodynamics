package electrodynamics.common.network;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.network.cable.type.IFluidPipe;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidUtilities {

	public static boolean isFluidReceiver(BlockEntity acceptor) {
		for (Direction dir : Direction.values()) {
			boolean is = isFluidReceiver(acceptor, dir);
			if (is) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFluidReceiver(BlockEntity acceptor, Direction dir) {
		if (acceptor != null) {
			if (acceptor.getCapability(ForgeCapabilities.FLUID_HANDLER, dir).isPresent()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isConductor(BlockEntity acceptor) {
		return acceptor instanceof IFluidPipe;
	}

	public static Integer receiveFluid(BlockEntity acceptor, Direction direction, FluidStack perReceiver, boolean debug) {
		if (isFluidReceiver(acceptor, direction)) {
			LazyOptional<IFluidHandler> cap = acceptor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
			if (cap.isPresent()) {
				IFluidHandler handler = cap.resolve().get();
				boolean canPass = false;
				for (int i = 0; i < handler.getTanks(); i++) {
					if (handler.isFluidValid(i, perReceiver)) {
						canPass = true;
						break;
					}
				}
				if (canPass) {
					return handler.fill(perReceiver, debug ? FluidAction.SIMULATE : FluidAction.EXECUTE);
				}
			}
		}
		return 0;
	}

	public static boolean canInputFluid(BlockEntity acceptor, Direction direction) {
		return isFluidReceiver(acceptor, direction);
	}

	public static void outputToPipe(GenericTile tile, FluidTank[] tanks, Direction... outputDirections) {
		Direction facing = tile.getFacing();
		for (Direction relative : outputDirections) {
			Direction direction = BlockEntityUtils.getRelativeSide(facing, relative.getOpposite());
			BlockPos face = tile.getBlockPos().relative(direction.getOpposite());
			BlockEntity faceTile = tile.getLevel().getBlockEntity(face);
			if (faceTile != null) {
				LazyOptional<IFluidHandler> cap = faceTile.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
				if (cap.isPresent()) {
					IFluidHandler fHandler = cap.resolve().get();
					for (FluidTank fluidTank : tanks) {
						FluidStack tankFluid = fluidTank.getFluid();
						int amtAccepted = fHandler.fill(tankFluid, FluidAction.EXECUTE);
						FluidStack taken = new FluidStack(tankFluid.getFluid(), amtAccepted);
						fluidTank.drain(taken, FluidAction.EXECUTE);
					}
				}
			}
		}
	}

	public static void drainItem(GenericTile tile, FluidTank[] tanks) {
		
		ComponentInventory inv = tile.getComponent(IComponentType.Inventory);
		
		int bucketIndex = inv.getInputBucketStartIndex();
		
		int size = inv.getInputBucketContents().size();
		
		if (tanks.length < size) {
			
			return;
			
		}
		
		int index;
		
		for (int i = 0; i < size; i++) {
			
			index = bucketIndex + i;
			
			FluidTank tank = tanks[i];
			ItemStack stack = inv.getItem(index).copy();

			if (stack.isEmpty() || CapabilityUtils.isFluidItemNull() || tank.getFluidAmount() >= tank.getCapacity()) {
				continue;
			}

			FluidStack containerFluid = CapabilityUtils.drainFluidItem(stack, Integer.MAX_VALUE, FluidAction.SIMULATE);

			if (!tank.isFluidValid(containerFluid)) {
				continue;
			}

			int amtDrained = tank.fill(containerFluid, FluidAction.SIMULATE);
			
			FluidStack drained = new FluidStack(containerFluid.getFluid(), amtDrained);
			
			CapabilityUtils.drainFluidItem(stack, drained, FluidAction.EXECUTE);
			
			tank.fill(drained, FluidAction.EXECUTE);
			
			if (stack.getItem() instanceof BucketItem) {
				
				inv.setItem(index, new ItemStack(Items.BUCKET));
				
			} else {
				
				inv.setItem(index, stack);
				
			}
		}

	}

	public static void fillItem(GenericTile tile, FluidTank[] tanks) {
		ComponentInventory inv = tile.getComponent(IComponentType.Inventory);
		List<ItemStack> buckets = inv.getOutputBucketContents();
		if (tanks.length >= buckets.size()) {
			for (int i = 0; i < buckets.size(); i++) {
				ItemStack stack = buckets.get(i);
				FluidTank tank = tanks[i];
				boolean isBucket = stack.getItem() instanceof BucketItem;
				if (!stack.isEmpty() && !CapabilityUtils.isFluidItemNull()) {
					FluidStack fluid = tank.getFluid();
					int amtFilled = CapabilityUtils.fillFluidItem(stack, fluid, FluidAction.SIMULATE);
					FluidStack taken = new FluidStack(fluid.getFluid(), amtFilled);
					boolean isWater = taken.getFluid().isSame(Fluids.WATER);
					if (isBucket && amtFilled == 1000 && (isWater || taken.getFluid().isSame(Fluids.LAVA))) {
						tank.drain(taken, FluidAction.EXECUTE);
						ItemStack filledBucket;
						if (isWater) {
							filledBucket = new ItemStack(Items.WATER_BUCKET);
						} else {
							filledBucket = new ItemStack(Items.LAVA_BUCKET);
						}
						inv.setItem(inv.getOutputBucketStartIndex() + i, filledBucket.copy());
					} else if (!isBucket) {
						CapabilityUtils.fillFluidItem(stack, taken, FluidAction.EXECUTE);
						tank.drain(taken, FluidAction.EXECUTE);
					}

				}
			}
		}
	}

	@Deprecated(since = "don't set a filter if you want to allow for all fluids")
	public static Fluid[] getAllRegistryFluids() {
		List<Fluid> list = new ArrayList<>(ForgeRegistries.FLUIDS.getValues());
		Fluid[] fluids = new Fluid[list.size()];
		for (int i = 0; i < list.size(); i++) {
			fluids[i] = list.get(i);
		}
		return fluids;
	}

}

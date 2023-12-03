package electrodynamics.prefab.utilities;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.network.cable.type.IConductor;
import electrodynamics.common.damage.DamageSources;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ElectricityUtils {

	public static void electrecuteEntity(Entity entityIn, TransferPack transfer) {
		if (transfer.getVoltage() <= 960.0) {
			Ingredient insulatingItems = Ingredient.of(ElectrodynamicsTags.Items.INSULATES_PLAYER_FEET);
			for (ItemStack armor : entityIn.getArmorSlots()) {
				if (ItemUtils.isIngredientMember(insulatingItems, armor.getItem())) {
					float damage = (float) transfer.getAmps() / 10.0f;
					if (Math.random() < damage) {
						int integerDamage = (int) Math.max(1, damage);
						if (armor.getDamageValue() > armor.getMaxDamage() || armor.hurt(integerDamage, entityIn.level.random, null)) {
							armor.setCount(0);
						}
					}
					return;
				}
			}
		}
		entityIn.hurt(DamageSources.ELECTRICITY, (float) Math.min(9999, Math.max(0, transfer.getAmps())));
	}

	public static boolean isElectricReceiver(BlockEntity tile) {
		for (Direction dir : Direction.values()) {
			boolean is = isElectricReceiver(tile, dir);
			if (is) {
				return true;
			}
		}
		return false;
	}

	public static boolean isElectricReceiver(BlockEntity tile, Direction dir) {
		if (tile != null) {
			if (tile.getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC, dir).isPresent() || tile.getCapability(CapabilityEnergy.ENERGY, dir).isPresent()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isConductor(BlockEntity acceptor, IConductor requesterWire) {
		return acceptor instanceof IConductor conductor;
	}

	public static TransferPack receivePower(BlockEntity tile, Direction direction, TransferPack transfer, boolean debug) {
		if (isElectricReceiver(tile, direction)) {
			LazyOptional<ICapabilityElectrodynamic> electro = tile.getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC, direction);
			if (electro.isPresent()) {
				ICapabilityElectrodynamic handler = electro.resolve().get();
				return handler.receivePower(transfer, debug);
			}
			LazyOptional<IEnergyStorage> fe = tile.getCapability(CapabilityEnergy.ENERGY, direction);
			if (fe.isPresent()) {
				IEnergyStorage handler = fe.resolve().get();
				TransferPack returner = TransferPack.joulesVoltage(handler.receiveEnergy((int) Math.min(Integer.MAX_VALUE, transfer.getJoules()), debug), transfer.getVoltage());
				if (transfer.getVoltage() > ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) {
					Level world = tile.getLevel();
					BlockPos pos = tile.getBlockPos();
					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					world.explode(null, pos.getX(), pos.getY(), pos.getZ(), (float) Math.log10(10 + transfer.getVoltage() / ElectrodynamicsCapabilities.DEFAULT_VOLTAGE), BlockInteraction.DESTROY);
				}
				return returner;
			}
		}
		return TransferPack.EMPTY;

	}

	public static boolean canInputPower(BlockEntity tile, Direction direction) {
		return isElectricReceiver(tile, direction);
	}

}

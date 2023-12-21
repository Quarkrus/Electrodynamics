package electrodynamics.common.item.gear.tools.electric;

import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.gear.tools.electric.utils.ElectricItemTier;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ItemElectricChainsaw extends DiggerItem implements IItemElectric {

	private final ElectricItemProperties properties;

	public ItemElectricChainsaw(ElectricItemProperties properties) {
		super(4, -2.4f, ElectricItemTier.DRILL, BlockTags.MINEABLE_WITH_AXE, properties.durability(0));
		this.properties = properties;
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		return true;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {

		if (!allowdedIn(group)) {
			return;
		}

		ItemStack charged = new ItemStack(this);
		IItemElectric.setEnergyStored(charged, properties.capacity);
		items.add(charged);
		
		ItemStack empty = new ItemStack(this);
		IItemElectric.setEnergyStored(empty, 0);
		items.add(empty);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return getJoulesStored(stack) > properties.extract.getJoules() ? super.getDestroySpeed(stack, state) : 0;
	}

	@Override
	public boolean canBeDepleted() {
		return false;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		extractPower(stack, properties.extract.getJoules(), false);
		return super.mineBlock(stack, worldIn, state, pos, entityLiving);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return (int) Math.round(13.0f * getJoulesStored(stack) / properties.capacity);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getJoulesStored(stack) < properties.capacity;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY));
		tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE), ChatFormatter.getChatDisplayShort(properties.extract.getVoltage(), DisplayUnit.VOLTAGE))).withStyle(ChatFormatting.RED));
		IItemElectric.addBatteryTooltip(stack, worldIn, tooltip);
	}

	@Override
	public ElectricItemProperties getElectricProperties() {
		return properties;
	}

	@Override
	public Item getDefaultStorageBattery() {
		return ElectrodynamicsItems.ITEM_BATTERY.get();
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

		if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
			return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
		}

		return true;

	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return getJoulesStored(stack) > properties.extract.getJoules() ? ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction) : false;
	}
}

package electrodynamics.common.item.gear.tools.electric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import electrodynamics.api.References;
import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.inventory.container.item.ContainerElectricDrill;
import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.common.item.gear.tools.electric.utils.ElectricItemTier;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemMultiDigger;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemElectricDrill extends ItemMultiDigger implements IItemElectric, CreativeTabSupplier {

	private final Supplier<CreativeModeTab> creativeTab;

	private static final List<ItemElectricDrill> DRILLS = new ArrayList<>();

	private static final Component CONTAINER_TITLE = Component.translatable("container.electricdrill");

	public static final int SLOT_COUNT = 3;

	public static final double POWER_USAGE = 1666666.66667 / (120.0 * 20.0);

	private static final String SUBTYPE = "subtype";
	private final ElectricItemProperties properties;

	public ItemElectricDrill(ElectricItemProperties properties, Supplier<CreativeModeTab> creativeTab) {
		super(4, -2.4f, ElectricItemTier.DRILL, properties.durability(0), BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_PICKAXE);
		this.properties = properties;
		this.creativeTab = creativeTab;
		DRILLS.add(this);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		return true;
	}

	@Override
	public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

		ItemStack empty = new ItemStack(this);
		IItemElectric.setEnergyStored(empty, 0);
		items.add(empty);

		ItemStack charged = new ItemStack(this);
		IItemElectric.setEnergyStored(charged, properties.capacity);
		items.add(charged);

	}

	@Override
	public boolean canBeDepleted() {
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getJoulesStored(stack) < properties.extract.getJoules()) {
			return 0;
		}

		float normalized = (float) Math.max(1, getHead(stack).speedBoost * getSpeedBoost(stack) / 2.0);

		return super.getDestroySpeed(stack, state) * normalized;

	}

	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		IItemElectric.setEnergyStored(stack, getJoulesStored(stack) - getPowerUsage(stack));

		// extractPower(stack, properties.extract.getJoules() * multiplier, false);
		return super.mineBlock(stack, worldIn, state, pos, entityLiving);
	}

	public double getPowerUsage(ItemStack stack) {
		double multiplier = Math.max(1, getSpeedBoost(stack));

		if (hasSilkTouch(stack)) {
			multiplier += 3;
		}

		int fortune = getFortuneLevel(stack);

		if (fortune > 0) {
			multiplier += fortune;
		}

		return POWER_USAGE * multiplier;
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
		tooltip.add(ElectroTextUtils.tooltip("electricdrill.miningspeed", ChatFormatter.getChatDisplayShort(getHead(stack).speedBoost * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
		tooltip.add(ElectroTextUtils.tooltip("electricdrill.usage", ChatFormatter.getChatDisplayShort(Math.max(getPowerUsage(stack), POWER_USAGE), DisplayUnit.JOULES).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
		double speedboost = getSpeedBoost(stack);
		if (speedboost > 1) {
			tooltip.add(ElectroTextUtils.tooltip("electricdrill.overclock", ChatFormatter.getChatDisplayShort(speedboost * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
		}
		int fortune = getFortuneLevel(stack);
		if (fortune > 0) {
			tooltip.add(ElectroTextUtils.tooltip("electricdrill.fortunelevel", fortune).withStyle(ChatFormatting.DARK_PURPLE));
		}
		if (hasSilkTouch(stack)) {
			tooltip.add(ElectroTextUtils.tooltip("electricdrill.silktouch").withStyle(ChatFormatting.DARK_PURPLE));
		}
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		if (getFortuneLevel(pStack) > 0 || hasSilkTouch(pStack)) {
			return true;
		}
		return super.isFoil(pStack);
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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

		if (!level.isClientSide) {

			player.openMenu(getMenuProvider(level, player, player.getItemInHand(hand)));

		}

		return super.use(level, player, hand);
	}

	public MenuProvider getMenuProvider(Level world, Player player, ItemStack stack) {
		return new SimpleMenuProvider((id, inv, play) -> {
		    IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		    if(handler == null) {
		        handler = new ItemStackHandler(SLOT_COUNT);
		    }
			return new ContainerElectricDrill(id, player.getInventory(), handler);
		}, CONTAINER_TITLE);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

		if (!other.isEmpty() && other.getItem() instanceof ItemDrillHead head) {

			ItemStack oldHead = new ItemStack(ElectrodynamicsItems.getItem(getHead(stack)));

			saveHead(stack, head.head);

			access.set(oldHead);

			player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ElectrodynamicsSounds.SOUND_BATTERY_SWAP.get(), SoundSource.PLAYERS, 0.25F, 1.0F, false);

			return true;

		}

		if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
			return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
		}

		return true;

	}

	@Override
	public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {

		int fortune = getFortuneLevel(stack);
		boolean silkTouch = hasSilkTouch(stack);

		Map<Enchantment, Integer> map = super.getAllEnchantments(stack);

		if (fortune > 0) {
			map.put(Enchantments.BLOCK_FORTUNE, fortune);
		}

		if (silkTouch) {
			map.put(Enchantments.SILK_TOUCH, 1);
		}

		return map;

	}

	public static int getFortuneLevel(ItemStack stack) {
		return stack.getOrCreateTag().getInt(NBTUtils.FORTUNE_ENCHANT);
	}

	public static boolean hasSilkTouch(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(NBTUtils.SILK_TOUCH_ENCHANT);
	}

	public static double getSpeedBoost(ItemStack stack) {
		return stack.getOrCreateTag().getDouble(NBTUtils.SPEED_ENCHANT);
	}

	public static SubtypeDrillHead getHead(ItemStack stack) {
		return SubtypeDrillHead.values()[stack.getOrCreateTag().getInt(SUBTYPE)];
	}

	public static void saveHead(ItemStack stack, SubtypeDrillHead head) {
		stack.getOrCreateTag().putInt(SUBTYPE, head.ordinal());
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	private static class ColorHandler {

		@SubscribeEvent
		public static void registerColoredBlocks(RegisterColorHandlersEvent.Item event) {
			DRILLS.forEach(item -> event.register((stack, index) -> {
				if (index == 1) {
					return getHead(stack).color.color();
				}
				return Color.WHITE.color();
			}, item));
		}

	}

	@Override
	public boolean isAllowedInCreativeTab(CreativeModeTab tab) {
		return creativeTab.get() == tab;
	}

	@Override
	public boolean hasCreativeTab() {
		return creativeTab != null;
	}

}

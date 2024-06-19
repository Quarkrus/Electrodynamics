package electrodynamics.common.item.gear.armor.types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import electrodynamics.api.References;
import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.render.model.armor.types.ModelCombatArmor;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsGases;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ItemCombatArmor extends ItemElectrodynamicsArmor implements IItemElectric {

    public static final String ARMOR_TEXTURE_LOCATION = References.ID + ":textures/model/armor/combatarmor.png";

    private final ElectricItemProperties properties;

    public static final float OFFSET = 0.2F;

    public ItemCombatArmor(Properties properties, Type type, Supplier<CreativeModeTab> creativeTab) {
        super(ItemCompositeArmor.CompositeArmor.COMPOSITE_ARMOR, type, properties, creativeTab);
        switch (type) {
        case HELMET, LEGGINGS:
            this.properties = (ElectricItemProperties) properties;
            break;
        default:
            this.properties = new ElectricItemProperties();
            break;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {
                ItemStack[] armorPiecesArray = new ItemStack[] { new ItemStack(ElectrodynamicsItems.ITEM_COMBATHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATBOOTS.get()) };

                List<ItemStack> armorPieces = new ArrayList<>();
                entity.getArmorSlots().forEach(armorPieces::add);

                boolean isBoth = armorPieces.get(0).getItem() == armorPiecesArray[3].getItem() && armorPieces.get(1).getItem() == armorPiecesArray[2].getItem();

                boolean hasChest = armorPieces.get(2).getItem() == armorPiecesArray[1].getItem();

                ModelCombatArmor<LivingEntity> model;

                if (isBoth) {
                    if (hasChest) {
                        model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_COMB_CHEST.bakeRoot(), getEquipmentSlot());
                    } else {
                        model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_COMB_NOCHEST.bakeRoot(), getEquipmentSlot());
                    }
                } else if (getEquipmentSlot() == EquipmentSlot.FEET) {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_BOOTS.bakeRoot(), getEquipmentSlot());
                } else if (hasChest) {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_LEG_CHEST.bakeRoot(), getEquipmentSlot());
                } else {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_LEG_NOCHEST.bakeRoot(), getEquipmentSlot());
                }

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        });
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {

        switch (getEquipmentSlot()) {
        case HEAD, LEGS:
            ItemStack empty = new ItemStack(this);
            IItemElectric.setEnergyStored(empty, 0);
            items.add(empty);

            ItemStack charged = new ItemStack(this);
            IItemElectric.setEnergyStored(charged, properties.capacity);
            items.add(charged);
            break;
        case CHEST:
            items.add(new ItemStack(this));
            if (ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM == null) {
                break;
            }
            ItemStack full = new ItemStack(this);

            GasStack gas = new GasStack(ElectrodynamicsGases.HYDROGEN.get(), ItemJetpack.MAX_CAPACITY, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL);

            IGasHandlerItem handler = full.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler == null) {
                return;
            }

            handler.fillTank(0, gas, GasAction.EXECUTE);

            CompoundTag tag = full.getOrCreateTag();
            tag.putInt(NBTUtils.PLATES, 2);

            items.add(full);
            break;
        case FEET:
            items.add(new ItemStack(this));

            if (Capabilities.FluidHandler.ITEM == null) {
                return;
            }

            full = new ItemStack(this);

            IFluidHandlerItem handlerFluid = full.getCapability(Capabilities.FluidHandler.ITEM);

            if (handlerFluid == null) {
                return;
            }

            ((RestrictedFluidHandlerItemStack) handlerFluid).setFluid(new FluidStack(ElectrodynamicsFluids.fluidHydraulic, ItemHydraulicBoots.MAX_CAPACITY));

            items.add(full);

            break;
        default:
            break;
        }

    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagin) {
        super.appendHoverText(stack, level, tooltip, flagin);
        switch (((ArmorItem) stack.getItem()).getEquipmentSlot()) {
        case HEAD:
            tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY));
            tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE), ChatFormatter.getChatDisplayShort(properties.extract.getVoltage(), DisplayUnit.VOLTAGE))).withStyle(ChatFormatting.RED));
            if (stack.hasTag() && stack.getTag().getBoolean(NBTUtils.ON)) {
                tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN)));
            } else {
                tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED)));
            }
            IItemElectric.addBatteryTooltip(stack, level, tooltip);
            break;
        case CHEST:
            ItemJetpack.staticAppendHoverText(stack, level, tooltip, flagin);
            ItemCompositeArmor.staticAppendHoverText(stack, level, tooltip, flagin);
            break;
        case LEGS:
            tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY));
            tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE), ChatFormatter.getChatDisplayShort(properties.extract.getVoltage(), DisplayUnit.VOLTAGE))).withStyle(ChatFormatting.RED));
            ItemServoLeggings.staticAppendTooltips(stack, level, tooltip, flagin);
            break;
        case FEET:
            if (Capabilities.FluidHandler.ITEM == null) {
                return;
            }
            IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

            if (handler == null) {
                return;
            }

            tooltip.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(handler.getFluidInTank(0).getAmount()), ChatFormatter.formatFluidMilibuckets(ItemHydraulicBoots.MAX_CAPACITY)).withStyle(ChatFormatting.GRAY));
            break;
        default:
            break;
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
        case HEAD:
            ItemNightVisionGoggles.armorTick(stack, world, player);
            break;
        case CHEST:
            ItemJetpack.armorTick(stack, world, player, OFFSET, true);
            break;
        case LEGS:
            ItemServoLeggings.armorTick(stack, world, player);
            break;
        default:
            break;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
        case HEAD, LEGS:
            return getJoulesStored(stack) < properties.capacity;
        case CHEST:
            return ItemJetpack.staticIsBarVisible(stack);
        case FEET:
            return ItemHydraulicBoots.staticIsBarVisible(stack);
        default:
            return false;
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
        case HEAD, LEGS:
            return (int) Math.round(13.0f * getJoulesStored(stack) / properties.capacity);
        case CHEST:
            return ItemJetpack.staticGetBarWidth(stack);
        case FEET:
            return ItemHydraulicBoots.staticGetBarWidth(stack);
        default:
            return 0;
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ARMOR_TEXTURE_LOCATION;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack1, ItemStack stack2) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public ElectricItemProperties getElectricProperties() {
        return properties;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ItemJetpack.staticCanElytraFly(stack, entity);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return ItemJetpack.staticElytraFlightTick(stack, entity, flightTicks);
    }

    @Override
    public Item getDefaultStorageBattery() {
        return switch (getEquipmentSlot()) {
        case HEAD, LEGS -> ElectrodynamicsItems.ITEM_BATTERY.get();
        default -> Items.AIR;
        };
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if (getEquipmentSlot() == EquipmentSlot.CHEST || getEquipmentSlot() == EquipmentSlot.FEET) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        return true;

    }

}

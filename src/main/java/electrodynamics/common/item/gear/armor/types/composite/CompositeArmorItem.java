package electrodynamics.common.item.gear.armor.types.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.References;
import electrodynamics.api.capability.ceramicplate.CapabilityCeramicPlate;
import electrodynamics.api.capability.ceramicplate.CapabilityCeramicPlateHolderProvider;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.render.model.armor.types.ModelCompositeArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CompositeArmorItem extends ArmorItem {

    private static final String ARMOR_TEXTURE_LOCATION = References.ID + ":textures/model/armor/compositearmor.png";

    public CompositeArmorItem(ArmorMaterial materialIn, EquipmentSlot slot) {
	super(materialIn, slot, new Item.Properties().stacksTo(1).tab(References.CORETAB).fireResistant().setNoRepair());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
	consumer.accept(new IItemRenderProperties() {
	    @Override
	    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, A properties) {

		ItemStack[] ARMOR_PIECES = new ItemStack[] { new ItemStack(DeferredRegisters.COMPOSITE_HELMET.get()),
			new ItemStack(DeferredRegisters.COMPOSITE_CHESTPLATE.get()), new ItemStack(DeferredRegisters.COMPOSITE_LEGGINGS.get()),
			new ItemStack(DeferredRegisters.COMPOSITE_BOOTS.get()) };

		List<ItemStack> armorPieces = new ArrayList<>();
		entity.getArmorSlots().forEach(armorPieces::add);

		boolean isBoth = ItemStack.isSameIgnoreDurability(armorPieces.get(0), ARMOR_PIECES[3])
			&& ItemStack.isSameIgnoreDurability(armorPieces.get(1), ARMOR_PIECES[2]);

		boolean hasChest = ItemStack.isSameIgnoreDurability(armorPieces.get(2), ARMOR_PIECES[1]);

		ModelCompositeArmor<LivingEntity> model;

		if (isBoth) {
		    if (hasChest) {
			model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_CHEST.bakeRoot(), slot);
		    } else {
			model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_NOCHEST.bakeRoot(), slot);
		    }
		} else if (slot == EquipmentSlot.FEET) {
		    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_BOOTS.bakeRoot(), slot);
		} else if (hasChest) {
		    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_CHEST.bakeRoot(), slot);
		} else {
		    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_NOCHEST.bakeRoot(), slot);
		}

		model.crouching = properties.crouching;
		model.riding = properties.riding;
		model.young = properties.young;

		return (A) model;
	    }
	});
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
	CompositeArmorItem item = (CompositeArmorItem) stack.getItem();
	if (EquipmentSlot.CHEST.equals(item.getSlot())) {
	    return new CapabilityCeramicPlateHolderProvider();
	}
	return null;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
	if (allowdedIn(group)) {
	    ItemStack filled = new ItemStack(this);
	    if (ItemStack.isSameIgnoreDurability(filled, new ItemStack(DeferredRegisters.COMPOSITE_CHESTPLATE.get()))) {
		filled.getCapability(CapabilityCeramicPlate.CERAMIC_PLATE_HOLDER_CAPABILITY).ifPresent(h -> h.increasePlateCount(2));
		items.add(filled);
	    }
	    ItemStack empty = new ItemStack(this);
	    items.add(empty);
	}
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
	return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
	return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
	super.appendHoverText(stack, worldIn, tooltip, flagIn);
	if (EquipmentSlot.CHEST.equals(getSlot())) {
	    stack.getCapability(CapabilityCeramicPlate.CERAMIC_PLATE_HOLDER_CAPABILITY).ifPresent(h -> {
		Component tip = new TranslatableComponent("tooltip.electrodynamics.ceramicplatecount", new TextComponent(h.getPlateCount() + ""))
			.withStyle(ChatFormatting.AQUA);
		tooltip.add(tip);
	    });
	}
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
	super.onArmorTick(stack, world, player);
	ItemStack[] pieces = new ItemStack[] { new ItemStack(DeferredRegisters.COMPOSITE_HELMET.get()),
		new ItemStack(DeferredRegisters.COMPOSITE_CHESTPLATE.get()), new ItemStack(DeferredRegisters.COMPOSITE_LEGGINGS.get()),
		new ItemStack(DeferredRegisters.COMPOSITE_BOOTS.get()) };

	List<ItemStack> armorPieces = new ArrayList<>();
	player.getArmorSlots().forEach(armorPieces::add);

	if (ItemStack.isSame(armorPieces.get(0), pieces[3]) || ItemStack.isSame(armorPieces.get(1), pieces[2])
		|| ItemStack.isSame(armorPieces.get(2), pieces[1]) || ItemStack.isSame(armorPieces.get(3), pieces[0])) {
	    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20));
	}
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defaultM) {
//
//	if (itemStack != ItemStack.EMPTY && itemStack.getItem() instanceof ArmorItem) {
//
//	    RenderCompositeArmor model = armorSlot == EquipmentSlot.LEGS ? new RenderCompositeArmor(0.5f) : new RenderCompositeArmor(1f);
//
//	    model.HEAD.visible = armorSlot == EquipmentSlot.HEAD;
//
//	    model.CHEST.visible = armorSlot == EquipmentSlot.CHEST;
//	    model.RIGHT_ARM.visible = armorSlot == EquipmentSlot.CHEST;
//	    model.LEFT_ARM.visible = armorSlot == EquipmentSlot.CHEST;
//
//	    model.RIGHT_LEG.visible = armorSlot == EquipmentSlot.LEGS;
//	    model.LEFT_LEG.visible = armorSlot == EquipmentSlot.LEGS;
//
//	    model.RIGHT_SHOE.visible = armorSlot == EquipmentSlot.FEET;
//	    model.LEFT_SHOE.visible = armorSlot == EquipmentSlot.FEET;
//
//	    model.young = defaultM.young;
//	    model.riding = defaultM.riding;
//	    model.crouching = defaultM.crouching;
//
//	    model.rightArmPose = defaultM.rightArmPose;
//	    model.leftArmPose = defaultM.leftArmPose;
//
//	    return (A) model;
//
//	}
//	return null;
//    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
	return ARMOR_TEXTURE_LOCATION;
    }
}

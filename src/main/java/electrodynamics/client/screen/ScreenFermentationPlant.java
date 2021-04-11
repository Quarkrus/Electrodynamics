package electrodynamics.client.screen;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.ElectricUnit;
import electrodynamics.api.gui.GenericScreen;
import electrodynamics.api.gui.component.GuiComponentElectricInfo;
import electrodynamics.api.gui.component.GuiComponentFluid;
import electrodynamics.api.gui.component.GuiComponentInfo;
import electrodynamics.api.gui.component.GuiComponentProgress;
import electrodynamics.api.gui.component.GuiComponentSlot;
import electrodynamics.api.gui.component.GuiComponentSlot.EnumSlotType;
import electrodynamics.common.inventory.container.ContainerFermentationPlant;
import electrodynamics.common.inventory.container.slot.SlotRestricted;
import electrodynamics.common.item.subtype.SubtypeProcessorUpgrade;
import electrodynamics.common.tile.TileFermentationPlant;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenFermentationPlant extends GenericScreen<ContainerFermentationPlant> {
    public ScreenFermentationPlant(ContainerFermentationPlant container, PlayerInventory playerInventory, ITextComponent title) {
	super(container, playerInventory, title);
	components.add(new GuiComponentProgress(() -> {
	    GenericTile furnace = container.getHostFromIntArray();
	    if (furnace != null) {
		ComponentProcessor processor = furnace.getComponent(ComponentType.Processor);
		if (processor.operatingTicks > 0) {
		    return Math.min(1.0, processor.operatingTicks / (processor.requiredTicks / 2.0));
		}
	    }
	    return 0;
	}, this, 42, 30));
	components.add(new GuiComponentProgress(() -> {
	    GenericTile furnace = container.getHostFromIntArray();
	    if (furnace != null) {
		ComponentProcessor processor = furnace.getComponent(ComponentType.Processor);
		if (processor.operatingTicks > 17) {
		    return Math.min(1.0, (processor.operatingTicks - processor.requiredTicks / 2.0) / (processor.requiredTicks / 2.0));
		}
	    }
	    return 0;
	}, this, 98, 30));
	components.add(new GuiComponentProgress(() -> 0, this, 42, 50).left());
	components.add(new GuiComponentFluid(() -> {
	    TileFermentationPlant boiler = container.getHostFromIntArray();
	    if (boiler != null) {
		ComponentFluidHandler handler = boiler.getComponent(ComponentType.FluidHandler);
		return handler.getTankFromFluid(Fluids.WATER);
	    }
	    return null;
	}, this, 21, 18));
	components.add(new GuiComponentFluid(() -> {
	    TileFermentationPlant boiler = container.getHostFromIntArray();
	    if (boiler != null) {
		ComponentFluidHandler handler = boiler.getComponent(ComponentType.FluidHandler);
		return handler.getTankFromFluid(DeferredRegisters.fluidEthanol);
	    }
	    return null;
	}, this, 127, 18));

	components.add(new GuiComponentElectricInfo(this::getEnergyInformation, this, -GuiComponentInfo.SIZE + 1, 2));
    }

    @Override
    protected GuiComponentSlot createGuiSlot(Slot slot) {
	return new GuiComponentSlot(slot instanceof SlotRestricted && ((SlotRestricted) slot)
		.isItemValid(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.basicspeed)))
			? EnumSlotType.SPEED
			: slot instanceof SlotRestricted ? EnumSlotType.LIQUID : EnumSlotType.NORMAL,
		this, slot.xPos - 1, slot.yPos - 1);
    }

    private List<? extends ITextProperties> getEnergyInformation() {
	ArrayList<ITextProperties> list = new ArrayList<>();
	GenericTile box = container.getHostFromIntArray();
	if (box != null) {
	    ComponentElectrodynamic electro = box.getComponent(ComponentType.Electrodynamic);
	    ComponentProcessor processor = box.getComponent(ComponentType.Processor);
	    list.add(new TranslationTextComponent("gui.fermentationplant.usage",
		    new StringTextComponent(ChatFormatter.getElectricDisplayShort(processor.getUsage() * 20, ElectricUnit.WATT))
			    .mergeStyle(TextFormatting.GRAY)).mergeStyle(TextFormatting.DARK_GRAY));
	    list.add(new TranslationTextComponent("gui.fermentationplant.voltage",
		    new StringTextComponent(ChatFormatter.getElectricDisplayShort(electro.getVoltage(), ElectricUnit.VOLTAGE))
			    .mergeStyle(TextFormatting.GRAY)).mergeStyle(TextFormatting.DARK_GRAY));
	}
	return list;
    }
}
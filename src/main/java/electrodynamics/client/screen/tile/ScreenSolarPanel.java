package electrodynamics.client.screen.tile;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerSolarPanel;
import electrodynamics.common.tile.generators.TileSolarPanel;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.TextUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSolarPanel extends GenericScreen<ContainerSolarPanel> {

	public ScreenSolarPanel(ContainerSolarPanel container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentMultiLabel(0, 0, stack -> {
			TileSolarPanel panel = menu.getHostFromIntArray();
			if(panel == null) {
				return;
			}
			TransferPack transfer = panel.getProduced();
			font.draw(stack, TextUtils.gui("machine.current", ChatFormatter.getChatDisplayShort(transfer.getAmps(), DisplayUnit.AMPERE)), (float) inventoryLabelX + 60, (float) inventoryLabelY - 48, 4210752);
			font.draw(stack, TextUtils.gui("machine.output", ChatFormatter.getChatDisplayShort(transfer.getWatts(), DisplayUnit.WATT)), (float) inventoryLabelX + 60, (float) inventoryLabelY - 35, 4210752);
			font.draw(stack, TextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(transfer.getVoltage(), DisplayUnit.VOLTAGE)), (float) inventoryLabelX + 60, (float) inventoryLabelY - 22, 4210752);
		}));
	}

}
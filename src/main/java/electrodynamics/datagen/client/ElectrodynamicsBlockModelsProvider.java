package electrodynamics.datagen.client;

import electrodynamics.api.References;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Unused class for now; do not remove please
 * 
 * @author skip999
 *
 */
public class ElectrodynamicsBlockModelsProvider extends BlockModelProvider {

	public ElectrodynamicsBlockModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, References.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {

	}

}

package electrodynamics.datagen.server;

import electrodynamics.api.References;
import electrodynamics.common.fluid.types.subtype.SubtypeSulfateFluid;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ElectrodynamicsFluidTagsProvider extends FluidTagsProvider {

	public ElectrodynamicsFluidTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		tag(ElectrodynamicsTags.Fluids.ETHANOL).add(ElectrodynamicsFluids.fluidEthanol);
		tag(ElectrodynamicsTags.Fluids.HYDROGEN_FLUORIDE).add(ElectrodynamicsFluids.fluidHydrogenFluoride);
		tag(ElectrodynamicsTags.Fluids.POLYETHLYENE).add(ElectrodynamicsFluids.fluidPolyethylene);
		tag(ElectrodynamicsTags.Fluids.SULFURIC_ACID).add(ElectrodynamicsFluids.fluidSulfuricAcid);
		for (SubtypeSulfateFluid sulfate : SubtypeSulfateFluid.values()) {
			tag(sulfate.tag).add(ElectrodynamicsFluids.SUBTYPEFLUID_REGISTRY_MAP.get(sulfate).get());
		}

	}

}

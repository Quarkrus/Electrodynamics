package electrodynamics.common.world.ruletest;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.common.block.BlockOre;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsRuleTestTypes;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class RuleTestOre extends RuleTest {

	@Nullable
	private final SubtypeOre thisOre;
	@Nullable
	private final SubtypeOreDeepslate thisDeepOre;

	private final TagKey<Block> canSpawnIn;

	public static final Codec<RuleTestOre> CODEC = RecordCodecBuilder.create(instance -> instance.group(Registry.BLOCK.byNameCodec().fieldOf("block").forGetter(instance0 -> {
		if (instance0.thisOre != null) {
			return ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(instance0.thisOre).get();
		}
		if (instance0.thisDeepOre != null) {
			return ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(instance0.thisDeepOre).get();
		}
		return Blocks.AIR;
	}), TagKey.codec(Registry.BLOCK_REGISTRY).fieldOf("canspawnintag").forGetter(instance0 -> instance0.canSpawnIn)).apply(instance, (block, tag) -> {
		if (block instanceof BlockOre ore) {
			return new RuleTestOre(ore.ore, ore.deepOre, tag);
		}
		return new RuleTestOre(null, null, tag);
	}));

	public RuleTestOre(SubtypeOre thisOre, SubtypeOreDeepslate thisDeepOre, TagKey<Block> canSpawnIn) {
		this.thisOre = thisOre;
		this.thisDeepOre = thisDeepOre;
		this.canSpawnIn = canSpawnIn;
	}

	@Override
	public boolean test(BlockState state, RandomSource random) {

		return isEnabled() && state.is(this.canSpawnIn);

	}

	private boolean isEnabled() {

		if (OreConfig.DISABLE_ALL_ORES) {
			return false;
		}

		if (thisOre != null) {

			if (OreConfig.DISABLE_STONE_ORES) {
				return false;
			}

			return switch (thisOre) {
			case aluminum -> OreConfig.SPAWN_ALUMINUM_ORE;
			case chromite -> OreConfig.SPAWN_CHROMIUM_ORE;
			case fluorite -> OreConfig.SPAWN_FLUORITE_ORE;
			case lead -> OreConfig.SPAWN_LEAD_ORE;
			case lepidolite -> OreConfig.SPAWN_LITHIUM_ORE;
			case molybdenum -> OreConfig.SPAWN_MOLYBDENUM_ORE;
			case monazite -> OreConfig.SPAWN_MONAZITE_ORE;
			case niter -> OreConfig.SPAWN_NITER_ORE;
			case halite -> OreConfig.SPAWN_SALT_ORE;
			case silver -> OreConfig.SPAWN_SILVER_ORE;
			case sulfur -> OreConfig.SPAWN_SULFUR_ORE;
			case sylvite -> OreConfig.SPAWN_SYLVITE_ORE;
			case thorianite -> OreConfig.SPAWN_THORIUM_ORE;
			case tin -> OreConfig.SPAWN_TIN_ORE;
			case rutile -> OreConfig.SPAWN_TITANIUM_ORE;
			case uraninite -> OreConfig.SPAWN_URANIUM_ORE;
			case vanadinite -> OreConfig.SPAWN_VANADIUM_ORE;
			default -> false;
			};
		}
		if (thisDeepOre != null) {

			if (OreConfig.DISABLE_DEEPSLATE_ORES) {
				return false;
			}

			return switch (thisDeepOre) {
			case aluminum -> OreConfig.SPAWN_DEEP_ALUMINUM_ORE;
			case chromite -> OreConfig.SPAWN_DEEP_CHROMIUM_ORE;
			case fluorite -> OreConfig.SPAWN_DEEP_FLUORITE_ORE;
			case lead -> OreConfig.SPAWN_DEEP_LEAD_ORE;
			case lepidolite -> OreConfig.SPAWN_DEEP_LITHIUM_ORE;
			case molybdenum -> OreConfig.SPAWN_DEEP_MOLYBDENUM_ORE;
			case monazite -> OreConfig.SPAWN_DEEP_MONAZITE_ORE;
			case niter -> OreConfig.SPAWN_DEEP_NITER_ORE;
			case halite -> OreConfig.SPAWN_DEEP_SALT_ORE;
			case silver -> OreConfig.SPAWN_DEEP_SILVER_ORE;
			case sulfur -> OreConfig.SPAWN_DEEP_SULFUR_ORE;
			case sylvite -> OreConfig.SPAWN_DEEP_SYLVITE_ORE;
			case thorianite -> OreConfig.SPAWN_DEEP_THORIUM_ORE;
			case tin -> OreConfig.SPAWN_DEEP_TIN_ORE;
			case rutile -> OreConfig.SPAWN_DEEP_TITANIUM_ORE;
			case uraninite -> OreConfig.SPAWN_DEEP_URANIUM_ORE;
			case vanadinite -> OreConfig.SPAWN_DEEP_VANADIUM_ORE;
			default -> false;
			};
		}
		return false;
	}

	@Override
	protected RuleTestType<?> getType() {
		return ElectrodynamicsRuleTestTypes.TEST_CONFIG_ORESPAWN.get();
	}

}

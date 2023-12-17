package electrodynamics.common.block.connect.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.common.collect.Maps;

import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public abstract class AbstractConnectBlock extends GenericEntityBlockWaterloggable {

	public static final Map<Direction, EnumProperty<EnumConnectType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), map -> {
		map.put(Direction.NORTH, EnumConnectType.NORTH);
		map.put(Direction.EAST, EnumConnectType.EAST);
		map.put(Direction.SOUTH, EnumConnectType.SOUTH);
		map.put(Direction.WEST, EnumConnectType.WEST);
		map.put(Direction.UP, EnumConnectType.UP);
		map.put(Direction.DOWN, EnumConnectType.DOWN);
	});

	public static final Map<Direction, EnumProperty<EnumConnectType>> DIRECTION_TO_CONNECTTYPE_MAP = Util.make(Maps.newEnumMap(Direction.class), map -> {
		map.put(Direction.UP, EnumConnectType.UP);
		map.put(Direction.DOWN, EnumConnectType.DOWN);
		map.put(Direction.NORTH, EnumConnectType.NORTH);
		map.put(Direction.EAST, EnumConnectType.EAST);
		map.put(Direction.SOUTH, EnumConnectType.SOUTH);
		map.put(Direction.WEST, EnumConnectType.WEST);
	});

	protected final VoxelShape[] boundingBoxes = new VoxelShape[7];

	protected HashMap<HashSet<Direction>, VoxelShape> shapestates = new HashMap<>();
	protected boolean locked = false;

	public AbstractConnectBlock(Properties properties, double radius) {
		super(properties);
		generateBoundingBoxes(radius);
	}

	public void generateBoundingBoxes(double radius) {
		double w = radius;
		double sm = 8 - w;
		double lg = 8 + w;
		// down
		boundingBoxes[0] = Block.box(sm, 0, sm, lg, lg, lg);
		// up
		boundingBoxes[1] = Block.box(sm, sm, sm, lg, 16, lg);
		// north
		boundingBoxes[2] = Block.box(sm, sm, 0, lg, lg, lg);
		// south
		boundingBoxes[3] = Block.box(sm, sm, sm, lg, lg, 16);
		// west
		boundingBoxes[4] = Block.box(0, sm, sm, lg, lg, lg);
		// east
		boundingBoxes[5] = Block.box(sm, sm, sm, 16, lg, lg);
		// center
		boundingBoxes[6] = Block.box(sm, sm, sm, lg, lg, lg);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape shape = boundingBoxes[6];
		HashSet<Direction> checked = new HashSet<>();

		for (Direction dir : Direction.values()) {

			if (!EnumConnectType.NONE.equals(state.getValue(DIRECTION_TO_CONNECTTYPE_MAP.get(dir)))) {
				checked.add(dir);
			}

		}
		locked = true;
		if (shapestates.containsKey(checked)) {
			locked = false;
			return shapestates.get(checked);
		}
		locked = false;
		for (Direction dir : checked) {
			if (dir != null) {
				shape = VoxelShapes.join(shape, boundingBoxes[dir.ordinal()], IBooleanFunction.OR);
			}
		}
		while (locked) {
			System.out.println("Bounding box collided with another block's bounding box!");
		}
		shapestates.put(checked, shape);
		if (shape == null) {
			return VoxelShapes.empty();
		}
		return shape;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(EnumConnectType.UP, EnumConnectType.DOWN, EnumConnectType.NORTH, EnumConnectType.EAST, EnumConnectType.SOUTH, EnumConnectType.WEST);
	}

}

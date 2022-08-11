package physica.nuclear.common.tile;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import physica.api.core.abstraction.Face;
import physica.api.core.inventory.IGuiInterface;
import physica.api.nuclear.IElectromagnet;
import physica.core.common.integration.IComputerIntegration;
import physica.library.energy.ElectricityUtilities;
import physica.library.energy.base.Unit;
import physica.library.location.GridLocation;
import physica.library.tile.TileBasePoweredContainer;
import physica.nuclear.client.gui.GuiParticleAccelerator;
import physica.nuclear.common.NuclearItemRegister;
import physica.nuclear.common.configuration.ConfigNuclearPhysics;
import physica.nuclear.common.entity.EntityParticle;
import physica.nuclear.common.inventory.ContainerParticleAccelerator;

public class TileParticleAccelerator extends TileBasePoweredContainer implements IGuiInterface, IElectromagnet, IComputerIntegration {

	public static final int		SLOT_INPUTCELLS					= 0;
	public static final int		SLOT_INPUTMATTER				= 1;
	public static final int		SLOT_OUTPUT						= 2;
	public static final int		POWER_USAGE						= ElectricityUtilities.convertEnergy(340000, Unit.WATT, Unit.RF);

	private static final int[]	ACCESSIBLE_SLOTS_UP				= new int[] { SLOT_INPUTMATTER };
	private static final int[]	ACCESSIBLE_SLOTS_DOWN			= new int[] { SLOT_OUTPUT };
	private static final int[]	ACCESSIBLE_SLOTS_MIDDLE_SIDES	= new int[] { SLOT_INPUTCELLS };

	protected EntityParticle	particle						= null;

	protected long				currentSessionUse				= 0;
	protected int				currentSessionTicks				= 0;
	protected double			velocity						= 0;
	protected int				antimatterAmount				= 0;
	private Item                lastInput;

	private int idleTicks;

	@Override
	public void onChunkUnload()
	{
		if (particle != null)
		{
			particle.setDead();
			particle = null;
			velocity = 0;
			currentSessionUse = 0;
		}
	}

	@Override
	public void updateServer(int ticks)
	{
		super.updateServer(ticks);

		ItemStack stackMatter = getStackInSlot(SLOT_INPUTMATTER);
		ItemStack stackEmptyCell = getStackInSlot(SLOT_INPUTCELLS);
		ItemStack stackOutputSlot = getStackInSlot(SLOT_OUTPUT);

		if (stackMatter != null) {
			if (stackMatter.getItem() != lastInput) {
				lastInput = stackMatter.getItem();
			}
		} else {
			lastInput = null;
		}

		if (idleTicks > 0) {
			idleTicks--;
			return;
		}

		if (particle != null)
		{
			if (hasEnoughEnergy() && isPoweredByRedstone())
			{
				velocity = particle.getTotalVelocity();

				if (stackEmptyCell != null)
				{
					if (stackEmptyCell.getItem() == NuclearItemRegister.itemEmptyElectromagneticCell)
					{
						if (antimatterAmount >= 125)
						{
							if (stackOutputSlot != null)
							{
								if(stackOutputSlot.getItem() == NuclearItemRegister.itemAntimatterCell125Milligram)
								{
									decrStackSize(SLOT_INPUTCELLS, 1);
									antimatterAmount -= 125;
									stackOutputSlot.stackSize++;
								}
							} else
							{
								decrStackSize(SLOT_INPUTCELLS, 1);
								setInventorySlotContents(SLOT_OUTPUT, new ItemStack(NuclearItemRegister.itemAntimatterCell125Milligram));
							}
						}
					}
				}
				if (currentSessionUse + 1 >= Long.MAX_VALUE)
				{
					currentSessionUse = 1;
				}
				currentSessionTicks += 1;
				currentSessionUse += extractEnergy();
				if (particle.isDead)
				{
					if (particle.didCollide())
					{
						if (stackEmptyCell != null)
						{
							if(stackEmptyCell.getItem() == NuclearItemRegister.itemEmptyQuantumCell)
							{
								if (World().rand.nextFloat() > 0.666f)
								{
									antimatterAmount = (int) Math.min(1000, antimatterAmount + (7 + World().rand.nextInt(5)) * (getParticleVelocity() / ConfigNuclearPhysics.ANTIMATTER_CREATION_SPEED));
									particle.setDead();
								}
								if (antimatterAmount > 100)
								{
									if (stackOutputSlot != null)
									{
										if (stackOutputSlot.getItem() == NuclearItemRegister.itemDarkmatterCell)
										{
											decrStackSize(SLOT_INPUTCELLS, 1);
											antimatterAmount = 0;
											stackOutputSlot.stackSize++;
										}
									} else
									{
										decrStackSize(SLOT_INPUTCELLS, 1);
										antimatterAmount = 0;
										setInventorySlotContents(SLOT_OUTPUT, new ItemStack(NuclearItemRegister.itemDarkmatterCell));
									}
								}
							}
						}
					}
					particle = null;
					velocity = 0;
				} else if (velocity >= ConfigNuclearPhysics.ANTIMATTER_CREATION_SPEED)
				{
					antimatterAmount = (int) Math.min(1000, antimatterAmount + (7 + World().rand.nextInt(5)) * (getParticleVelocity() / ConfigNuclearPhysics.ANTIMATTER_CREATION_SPEED));
					particle.setDead();
					particle = null;
					velocity = 0;
				}
			} else
			{
				particle.setDead();
				particle = null;
				velocity = 0;
				currentSessionUse = 0;
				currentSessionTicks = 0;
				if (!hasEnoughEnergy()) {
					idleTicks = 80;
				}
			}
		} else if (isPoweredByRedstone() && hasEnoughEnergy())
		{
			Face opposite = getFacing().getOpposite();
			GridLocation loc = getLocation();
			if (stackMatter != null && stackEmptyCell != null && EntityParticle.canSpawnParticle(World(), loc.xCoord + opposite.offsetX, loc.yCoord + opposite.offsetY, loc.zCoord + opposite.offsetZ))
			{
				currentSessionUse = extractEnergy();

				particle = new EntityParticle(World(), loc.xCoord + opposite.offsetX, loc.yCoord + opposite.offsetY, loc.zCoord + opposite.offsetZ, opposite);
				World().spawnEntityInWorld(particle);

				decrStackSize(SLOT_INPUTMATTER, 1);
			}
		}
	}

	public static void main(String[] args)
	{
		double speed = 0.001;
		double travelled = 0.5f;
		int ticks = 0;
		int stopped = 0;
		while (speed < 1)
		{
			speed += 0.002;
			travelled += speed;
			if ((int) travelled % 50 == 49)
			{
				speed *= 0.9075f;
				stopped++;
				if (stopped > 32)
				{
					System.out.println("Never hit a max.");
					break;
				}
			}
			if (speed > 1)
			{
				break;
			}
			ticks++;
		}
		System.out.println("Corner hits: " + stopped + ", Speed: " + speed + ", Ticks: " + ticks + ", Seconds: " + ticks / 20 + ", Travelled: " + travelled);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateClient(int ticks)
	{
		super.updateClient(ticks);
		if (particle != null)
		{
			if (particle.isDead)
			{
				particle = null;
			}
		}
	}

	public AcceleratorStatus getAcceleratorStatus()
	{
		if (particle != null)
		{
			if (hasEnoughEnergy() && isPoweredByRedstone())
			{
				if (particle.isDead)
				{
					if (particle.didCollide())
					{
						return AcceleratorStatus.Done;
					}
					return AcceleratorStatus.Failure;
				} else if (velocity > ConfigNuclearPhysics.ANTIMATTER_CREATION_SPEED)
				{
					return AcceleratorStatus.Done;
				} else
				{
					return AcceleratorStatus.Accelerating;
				}
			}
			return AcceleratorStatus.Failure;
		} else if (isPoweredByRedstone() && hasEnoughEnergy())
		{
			Face opposite = getFacing().getOpposite();
			GridLocation loc = getLocation();
			if (EntityParticle.canSpawnParticle(World(), loc.xCoord + opposite.offsetX, loc.yCoord + opposite.offsetY, loc.zCoord + opposite.offsetZ))
			{
				return AcceleratorStatus.Ready;
			}
			return AcceleratorStatus.Failure;
		} else if (!hasEnoughEnergy())
		{
			return AcceleratorStatus.Disabled;
		} else
		{
			return AcceleratorStatus.Idle;
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if (particle != null)
		{
			particle.setDead();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("currentSessionUse", currentSessionUse);
		nbt.setInteger("currentSessionTicks", currentSessionTicks);
		nbt.setInteger("antimatterAmount", antimatterAmount);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		currentSessionUse = nbt.getLong("currentSessionUse");
		currentSessionTicks = nbt.getInteger("currentSessionTicks");
		antimatterAmount = nbt.getInteger("antimatterAmount");
	}

	@Override
	public void writeClientGuiPacket(List<Object> dataList, EntityPlayer player)
	{
		super.writeClientGuiPacket(dataList, player);
		dataList.add(currentSessionUse);
		dataList.add(currentSessionTicks);
		dataList.add(antimatterAmount);
		dataList.add(velocity);
	}

	@Override
	public void readClientGuiPacket(ByteBuf buf, EntityPlayer player)
	{
		super.readClientGuiPacket(buf, player);
		currentSessionUse = buf.readLong();
		currentSessionTicks = buf.readInt();
		antimatterAmount = buf.readInt();
		velocity = buf.readDouble();
	}

	@Override
	public int getSizeInventory()
	{
		return 3;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return stack == null || slot == SLOT_OUTPUT ? false
				: slot == SLOT_INPUTMATTER && stack.isStackable() || slot == SLOT_INPUTCELLS && (stack.getItem() == NuclearItemRegister.itemEmptyElectromagneticCell || stack.getItem() == NuclearItemRegister.itemEmptyQuantumCell);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getClientGuiElement(int id, EntityPlayer player)
	{
		return new GuiParticleAccelerator(player, this);
	}

	@Override
	public Container getServerGuiElement(int id, EntityPlayer player)
	{
		return new ContainerParticleAccelerator(player, this);
	}

	public enum AcceleratorStatus {
		Disabled("No Power"), Idle("Idle"), Accelerating("Accelerating"), Done("Done"), Ready("Ready"), Failure("Failure");

		private String str;

		AcceleratorStatus(String str){
			this.str = str;
		}

		public String getDisplayString() {
			return this.str;
		}
	}

	@Override
	public boolean canConnectElectricity(Face from)
	{
		return from != getFacing().getOpposite();
	}

	@Override
	public int getPowerUsage()
	{
		return POWER_USAGE;
	}

	public float getParticleVelocity()
	{
		return (float) velocity;
	}

	public float getParticleMaxVelocity()
	{
		return ConfigNuclearPhysics.ANTIMATTER_CREATION_SPEED;
	}

	public long getSessionUse()
	{
		return currentSessionUse;
	}

	public int getCurrentSessionTicks()
	{
		return currentSessionTicks;
	}

	@Override
	public int[] getAccessibleSlotsFromFace(Face face)
	{
		return face == Face.DOWN ? ACCESSIBLE_SLOTS_DOWN : face == Face.UP ? ACCESSIBLE_SLOTS_UP : ACCESSIBLE_SLOTS_MIDDLE_SIDES;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, Face face)
	{
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, Face face)
	{
		return slot == SLOT_OUTPUT;
	}

	public int getAntimatterAmount()
	{
		return antimatterAmount;
	}

	public EntityParticle getParticle()
	{
		return particle;
	}

	public void setParticle(EntityParticle particle)
	{
		this.particle = particle;
	}

	public String getStatus()
	{
		switch (this.getAcceleratorStatus()) {
			case Disabled:
				return "disabled";
			case Idle:
				return "idle";
			case Accelerating:
				return "accelerating";
			case Done:
				return "done";
			case Ready:
				return "ready";
			case Failure:
				return "failure";
			default:
				return null;
		}
	}

	@Override
	public String getComponentName() {
		return "particle_accelerator";
	}

	@Override
	public String[] methods()
	{
		return new String[]{"getStatus", "getAntimatterAmount", "getParticleMaxVelocity", "getParticleVelocity"};
	}

	@Override
	public Object[] invoke(int method, Object[] args) throws Exception
	{
		switch (method) {
			case 0:
				return new Object[]{this.getStatus()};
			case 1:
				return new Object[]{this.getAntimatterAmount()};
			case 2:
				return new Object[]{this.getParticleMaxVelocity()};
			case 3:
				return new Object[]{this.getParticleVelocity()};
			default:
				throw new NoSuchMethodException();
		}
	}
}

package electrodynamics.common.packet.types.client;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSpawnSmokeParticle {

	private final BlockPos pos;

	public PacketSpawnSmokeParticle(BlockPos pos) {
		this.pos = pos;
	}

	public static void handle(PacketSpawnSmokeParticle message, Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			ClientWorld world = Minecraft.getInstance().level;
			if (world != null) {
				world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, message.pos.getX() + 0.5, message.pos.getY() + 0.5, message.pos.getZ() + 0.5, 0, 0, 0);
			}
		});
		ctx.setPacketHandled(true);
	}

	public static void encode(PacketSpawnSmokeParticle pkt, PacketBuffer buf) {
		buf.writeBlockPos(pkt.pos);
	}

	public static PacketSpawnSmokeParticle decode(PacketBuffer buf) {
		return new PacketSpawnSmokeParticle(buf.readBlockPos());
	}
}
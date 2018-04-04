package net.simpleAPI.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.simpleAPI.attributes.listener.SPacketChunkAttr;
import net.simpleAPI.attributes.listener.SPacketWorldAttr;

import java.util.Map;
import java.util.UUID;

/**
 * @author ci010
 */
public class AttributeAPI extends DummyModContainer {
	public AttributeAPI() {
		super(new ModMetadata());
		ModMetadata m = getMetadata();
		m.modId = "attributesapi";
		m.name = "AttributeAPI";
		m.description = "Provide the auto syncable attribute throw capability.";
		m.version = "0.0.1";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
		System.out.println("reg this");
		MinecraftForge.EVENT_BUS.register(this);
	}

	private Map<Integer, Multimap<ChunkPos, UUID>> worldPlayerToChunks = null;

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load load) {
		if (!load.getWorld().isRemote) {
			if (worldPlayerToChunks == null) worldPlayerToChunks = Maps.newHashMap();
			worldPlayerToChunks.put(load.getWorld().provider.getDimension(), HashMultimap.create());
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if (!event.getWorld().isRemote) {
			if (worldPlayerToChunks == null) return;
			worldPlayerToChunks.remove(event.getWorld().provider.getDimension());
		}
	}

	@SubscribeEvent
	public void onChunkWatch(ChunkWatchEvent.Watch e) {
		World world = e.getPlayer().world;
		Multimap<ChunkPos, UUID> chunkToPlayers = worldPlayerToChunks.get(world.provider.getDimension());
		chunkToPlayers.put(e.getChunk(), e.getPlayer().getUniqueID());

		EntityPlayerMP mp = e.getPlayer();
		Chunk c = e.getPlayer().world.getChunkFromChunkCoords(e.getChunk().x, e.getChunk().z);
		AttributeView cap = c.getCapability(AttributeView.CAPABILITY, null);
		if (cap == null) return;
		ImmutableList<Var<?>> all = cap.getVarsByMode(UpdateMode.CONSTANTLY);
		if (all.size() == 0) return;
		NBTTagCompound compound = new NBTTagCompound();
		for (Var<?> var : all) {
			VarSyncable<?> syncable = (VarSyncable<?>) var;
			syncable.writeToNBT(compound);
		}
		mp.connection.sendPacket(new SPacketChunkAttr(e.getChunk().x, e.getChunk().z, compound));
	}

	@SubscribeEvent
	public void onChunkUnwatch(ChunkWatchEvent.UnWatch e) {
		World world = e.getPlayer().world;
		Multimap<ChunkPos, UUID> chunkToPlayers = worldPlayerToChunks.get(world.provider.getDimension());
		chunkToPlayers.remove(e.getChunk(), e.getPlayer().getUniqueID());
	}

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP mp = (EntityPlayerMP) event.getEntity();
			World world = mp.world;
			AttributeView cap = world.getCapability(AttributeView.CAPABILITY, null);
			if (cap == null) return;
			ImmutableList<Var<?>> all = cap.getVarsByMode(UpdateMode.CONSTANTLY);
			if (all.size() == 0) return;
			NBTTagCompound compound = new NBTTagCompound();
			for (Var<?> var : all) {
				VarSyncable<?> syncable = (VarSyncable<?>) var;
				syncable.writeToNBT(compound);
			}
			mp.connection.sendPacket(new SPacketWorldAttr(compound));
		}
	}


	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
			/*
			 * general polling for worlds and chunks
			 */
			{
				AttributeView capability = event.world.getCapability(AttributeView.CAPABILITY, null);
				if (capability != null) {
					ImmutableList<Var<?>> vars = capability.getVarsByMode(UpdateMode.CONSTANTLY);
					NBTTagCompound tag = new NBTTagCompound();
					for (Var<?> var : vars) {
						VarSyncable<?> s = (VarSyncable<?>) var;
						if (s.dirty()) {
							s.writeToNBT(tag);
						}
					}
					if (!tag.hasNoTags()) {
						for (EntityPlayer player : event.world.playerEntities) {
							((EntityPlayerMP) player).connection.sendPacket(new SPacketWorldAttr(tag));
						}
					}
				}
			}

			Multimap<ChunkPos, UUID> multimap = worldPlayerToChunks.get(event.world.provider.getDimension());

			for (ChunkPos pos : multimap.keySet()) {
				Chunk chunk = event.world.getChunkFromChunkCoords(pos.x, pos.z);
				AttributeView capability = chunk.getCapability(AttributeView.CAPABILITY, null);
				if (capability == null) continue;
				ImmutableList<Var<?>> vars = capability.getVarsByMode(UpdateMode.CONSTANTLY);
				if (vars.size() == 0) continue;

				NBTTagCompound tag = new NBTTagCompound();
				for (Var<?> var : vars) {
					VarSyncable<?> s = (VarSyncable<?>) var;
					if (s.dirty()) {
						s.writeToNBT(tag);
					}
				}
				if (!tag.hasNoTags()) {
					for (UUID uuid : multimap.get(pos)) {
						EntityPlayerMP mp = (EntityPlayerMP) event.world.getPlayerEntityByUUID(uuid);
						mp.connection.sendPacket(new SPacketChunkAttr(pos.x, pos.z, tag));
					}
				}
			}
		}
	}
}

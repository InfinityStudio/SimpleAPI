package net.simpleAPI.attributes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simpleAPI.attributes.*;
import net.simpleAPI.capabilities.Capabilities;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ci010
 */
@Mod(modid = "test-lib", version = "0", name = "Test")
public class TestAttribute {
	private static TestUnit[] client = new TestUnit[3], server = new TestUnit[3];

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	}

	static class TestUnit {
		String name;
		Var<Number> testNumConst, testNumLazy;

		TestUnit(String name, AttachCapabilitiesEvent event) {
			this.name = name;
			AttributeFactory factory = AttributeFactory.create(event.getObject());
			testNumConst = factory.newNumber("testNumConst", 1, UpdateMode.CONSTANTLY);
			testNumLazy = factory.newNumber("testNumLazy", 1, UpdateMode.LAZY);
//			factory.newString("testStringLazy", "1", UpdateMode.LAZY);
//			factory.newString("testStringConst", "1", UpdateMode.CONSTANTLY);
			System.out.println("add capability to " + name);
			event.addCapability(new ResourceLocation("test", "attri"),
					Capabilities.newBuilder(AttributeView.CAPABILITY).append(factory.build()).build());
		}

		void step() {
			testNumConst.set(testNumConst.get().intValue() + 1);
			testNumLazy.set(testNumLazy.get().floatValue() + 0.1);
			System.out.println(this);
		}

		@Override
		public String toString() {
			return name + ": const " + testNumConst.get() + " " + ", lazy " + testNumLazy.get();
		}

	}

	@Mod.EventBusSubscriber
	public static class Subscribe {
		@SubscribeEvent
		public static void chunk(AttachCapabilitiesEvent<Chunk> event) {
			AttributeFactory factory = AttributeFactory.create(event.getObject());
			factory.newNumber("testNumConst", 1, UpdateMode.CONSTANTLY);
			factory.newNumber("testNumLazy", 1, UpdateMode.LAZY);
			event.addCapability(new ResourceLocation("test", "attri"),
					Capabilities.newBuilder(AttributeView.CAPABILITY).append(factory.build()).build());
		}

		@SubscribeEvent
		public static void world(AttachCapabilitiesEvent<World> event) {
			AttributeFactory factory = AttributeFactory.create(event.getObject());
			factory.newNumber("testNumConst", 1, UpdateMode.CONSTANTLY);
			factory.newNumber("testNumLazy", 1, UpdateMode.LAZY);
			event.addCapability(new ResourceLocation("test", "attri"),
					Capabilities.newBuilder(AttributeView.CAPABILITY).append(factory.build()).build());
		}

		@SubscribeEvent
		public static void init(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityPlayer) {
				System.out.println("Attach to player!");
				if (event.getObject().world.isRemote) {
					client[0] = (new TestUnit("player", event));
				}
				else {
					server[0] = (new TestUnit("player", event));
				}
			}
		}

		private static KeyBinding binding = new KeyBinding("test", Keyboard.KEY_I, "");

		@SideOnly(Side.CLIENT)
		private static void reportClient() {
			Minecraft mc = Minecraft.getMinecraft();
			int x = mc.player.chunkCoordX, y = mc.player.chunkCoordZ;
			Chunk c = mc.world.getChunkFromChunkCoords(x, y);
			System.out.println("@CLIENT");
			AttributeView cap = c.getCapability(AttributeView.CAPABILITY, null);
			if (cap != null) {
				Collection<? extends Var<?>> all = cap.getVarsByMode(UpdateMode.CONSTANTLY);
				System.out.println("CHUNK: ");
				for (Var<?> var : all) {
					System.out.println(var);
				}
				System.out.println();
			}
			cap = mc.world.getCapability(AttributeView.CAPABILITY, null);
			if (cap != null) {
				Collection<? extends Var<?>> all = cap.getVarsByMode(UpdateMode.CONSTANTLY);
				System.out.println("WORLD: ");
				for (Var<?> var : all) {
					System.out.println(var);
				}
				System.out.println();
			}
			cap = mc.player.getCapability(AttributeView.CAPABILITY, null);
			if (cap != null) {
				Collection<? extends Var<?>> all = cap.getVarsByMode(UpdateMode.CONSTANTLY);
				System.out.println("PLAYER: ");
				for (Var<?> var : all) {
					System.out.println(var);
				}
				System.out.println();
			}
		}

		@SubscribeEvent
		public static void interact(PlayerInteractEvent event) {
			System.out.println("interact@" + (event.getWorld().isRemote ? "Client" : "Server"));
			if (event.getWorld().isRemote)
				reportClient();
			else {
				Var<Number> testNumConst, testNumLazy;
				EntityPlayer p = event.getEntityPlayer();
				AttributeView cap = p.getCapability(AttributeView.CAPABILITY, null);

				testNumConst = (Var<Number>) cap.getByName("testNumConst");
				testNumLazy = (Var<Number>) cap.getByName("testNumLazy");
				testNumConst.set(testNumConst.get().intValue() + 1);
				testNumLazy.set(testNumLazy.get().floatValue() + 0.1);

				cap = event.getWorld().getCapability(AttributeView.CAPABILITY, null);
				testNumConst = (Var<Number>) cap.getByName("testNumConst");
				testNumLazy = (Var<Number>) cap.getByName("testNumLazy");
				testNumConst.set(testNumConst.get().intValue() + 1);
				testNumLazy.set(testNumLazy.get().floatValue() + 0.1);


				cap = event.getWorld().getChunkFromChunkCoords(p.chunkCoordX, p.chunkCoordZ).getCapability(AttributeView.CAPABILITY, null);
				testNumConst = (Var<Number>) cap.getByName("testNumConst");
				testNumLazy = (Var<Number>) cap.getByName("testNumLazy");
				testNumConst.set(testNumConst.get().intValue() + 1);
				testNumLazy.set(testNumLazy.get().floatValue() + 0.1);

			}
		}
	}
}

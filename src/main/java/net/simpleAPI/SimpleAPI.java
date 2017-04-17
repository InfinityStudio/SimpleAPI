package net.simpleAPI;

import net.simpleAPI.impl.ModComponentBatch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * @author ci010
 */
@Mod(modid = "simple_api")
public class SimpleAPI
{
	String test = "{\n" +
			"  \"id\": \"test_item\",\n" +
			"  \"creativeTab\": \"decorations\",\n" +
			"  \"type\": \"stack\",\n" +
			"  \"maxStackSize\": 32\n" +
			"}";

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModComponentBatch batch = new ModComponentBatch("simple_api", Side.CLIENT);
//		File mcDir = Loader.instance().getConfigDir().getParentFile();
//		File defines = new File(mcDir, "defines");
//
//		File[] files = defines.listFiles();
//		if (files != null)
//			for (File file : files)
//				if (isTargetFile(file))
//				{
//
//				}
	}

	private boolean isTargetFile(File file)
	{
		return file.isFile() && file.getName().endsWith(".zip");
	}
}

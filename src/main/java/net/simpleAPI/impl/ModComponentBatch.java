package net.simpleAPI.impl;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simpleAPI.impl.item.ItemAdapterBase;
import net.simpleAPI.impl.item.ItemFoodAdapter;
import net.simpleAPI.impl.item.ItemToolAdapter;
import net.simpleAPI.impl.reg.RegComponent;
import net.simpleAPI.impl.reg.RegItem;
import net.simpleAPI.item.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * @author ci010
 */
public class ModComponentBatch
{
	private Side side;

	private List<RegComponent> components = new ArrayList<RegComponent>();

	public ModComponentBatch(Side side)
	{
		this.side = side;
	}

	public void register()
	{
		for (RegComponent component : components)
			component.register();
		if (side == Side.CLIENT)
			for (RegComponent component : components)
				component.registerClient();
	}

	public void compile(String js) throws ScriptException
	{
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(js);
	}
}

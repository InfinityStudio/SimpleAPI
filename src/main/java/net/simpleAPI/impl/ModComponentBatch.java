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

	/////Item codes... will be move to other class later. Just for write and run now/////
	public <T extends Item> void addItem(String modid, String json)
	{
		try
		{
			JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
			Item item = createItem(obj);
			String id = obj.get("id").getAsString();
			if (side.isClient() && obj.has("creativeTab"))
				item.setCreativeTab(find(MCJsonUtil.getString(obj, "creativeTab", null)));
			item.setUnlocalizedName(modid + "." + id).setRegistryName(modid, id);
			components.add(new RegItem(item));
		}
		catch (Exception e) {e.printStackTrace();}
	}

	private ItemAdapterBase link(ItemAdapterBase itemAdapter, Object handler)
	{
		if (handler == null)
		{
			//TODO log
			return itemAdapter;
		}
		if (handler instanceof ItemBlockDestroy)
			itemAdapter.setDestroy((ItemBlockDestroy) handler);
		if (handler instanceof ItemBlockInteract)
			itemAdapter.setBlockInteract((ItemBlockInteract) handler);
		if (handler instanceof ItemEntityInteract)
			itemAdapter.setInteract((ItemEntityInteract) handler);
		if (handler instanceof ItemCreate)
			itemAdapter.setCreate((ItemCreate) handler);
		if (handler instanceof ItemEntityHandler)
			itemAdapter.setHandler((ItemEntityHandler) handler);
		if (handler instanceof ItemUpdate)
			itemAdapter.setUpdate((ItemUpdate) handler);
		if (handler instanceof ItemUsing)
			itemAdapter.setUsing((ItemUsing) handler);
		return itemAdapter;
	}


	private Item createItem(JsonObject object)
	{
		String type = MCJsonUtil.getString(object, "type", "stack");
		if (type.equals("stack"))
			return handleStack(object, new ItemAdapterBase())
					.setCapabilityFactory(CapabilityBinding.INSTANCE.createCapabilitiesFactory(object.get("capabilities")));
		else if (type.equals("consumable"))
			return handleConsume(object, new ItemAdapterBase())
					.setCapabilityFactory(CapabilityBinding.INSTANCE.createCapabilitiesFactory(object.get("capabilities")));
		else if (type.equals("tool"))
			return new ItemToolAdapter(ofNullable(object.get("attackDamage")).map(JsonElement::getAsFloat).orElse
					(0F),
					ofNullable(object.get("attackSpeed")).map(JsonElement::getAsFloat).orElse(0F),
					ofNullable(object.get("material")).filter(JsonElement::isJsonPrimitive)
							.map(input -> Item.ToolMaterial.valueOf(input.getAsString())
							).orElse(Item.ToolMaterial.IRON),
					ofNullable(object.get("effectiveBlocks")).filter(JsonElement::isJsonArray)
							.map(JsonElement::getAsJsonArray).map(arr ->
					{
						Set<Block> blockSet = Sets.newHashSet();
						for (JsonElement jsonElement : arr)
							if (jsonElement.isJsonPrimitive())
								blockSet.add(Block.getBlockFromName(jsonElement.getAsString()));
						return blockSet;
					}).orElse(Collections.emptySet())).setCapabilityFactory(CapabilityBinding.INSTANCE.createCapabilitiesFactory(object.get("capabilities")));
		else if (type.equals("food"))
			return handleStack(object, new ItemFoodAdapter(
					ofNullable(object.get("healAmount")).map(JsonElement::getAsInt).orElse(0),
					ofNullable(object.get("saturation")).map(JsonElement::getAsFloat).orElse(0F),
					ofNullable(object.get("isWolfFood")).map(JsonElement::getAsBoolean).orElse(false)
			)).setCapabilityFactory(CapabilityBinding.INSTANCE.createCapabilitiesFactory(object.get("capabilities")));
		return null;
	}

	private <T extends Item> T handleToolTip(JsonObject object, T item)
	{
		//TODO
		return item;
	}

	private <T extends Item> T handleConsume(JsonObject object, T item)
	{
		item.setMaxDamage(MCJsonUtil.getInt(object, "maxDamage", 0));
		return item;
	}

	private <T extends Item> T handleStack(JsonObject object, T item)
	{
		int maxMeta = MCJsonUtil.getInt(object, "maxMeta", 0);
		if (maxMeta > 0) item.setHasSubtypes(true);
		item.setMaxStackSize(MCJsonUtil.getInt(object, "maxStackSize", 64)).setMaxDamage(0);
		return item;
	}

	@SideOnly(Side.CLIENT)
	private CreativeTabs find(String id)
	{
		if (id == null) return CreativeTabs.MISC;
		for (CreativeTabs tabs : CreativeTabs.CREATIVE_TAB_ARRAY)
			if (tabs.getTabLabel().equals(id))
				return tabs;
		//TODO log
		return CreativeTabs.MISC;
	}
}

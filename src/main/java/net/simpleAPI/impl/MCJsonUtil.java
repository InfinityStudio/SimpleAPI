package net.simpleAPI.impl;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;

/**
 * @author ci010
 */
public class MCJsonUtil
{
	public static int getInt(JsonObject json, String memberName, int fallback)
	{
		return json.has(memberName) ? JsonUtils.getInt(json.get(memberName), memberName) : fallback;
	}

	public static boolean getBool(JsonObject json, String memberName, boolean fallback)
	{
		return json.has(memberName) ? JsonUtils.getBoolean(json.get(memberName), memberName) : fallback;
	}

	public static String getString(JsonObject json, String memberName, String fallback)
	{
		return json.has(memberName) ? JsonUtils.getString(json.get(memberName), memberName) : fallback;
	}
}

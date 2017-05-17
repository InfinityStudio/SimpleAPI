package net.simpleAPI.ui.element;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ci010
 */
public class Group extends Element
{
	private List<Element> children = Collections.emptyList();

	private ResourceLocation background;

	public ResourceLocation getBackground() {return background;}

	public void setBackground(ResourceLocation background)
	{
		this.background = background;
	}

	public List<Element> getChildren()
	{
		if (children == Collections.EMPTY_LIST)
			children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString()
	{
		return "Group{" +
				(background == null ? "" : "background=" + background + ", ") +
				"children=" + children +
				'}';
	}
}

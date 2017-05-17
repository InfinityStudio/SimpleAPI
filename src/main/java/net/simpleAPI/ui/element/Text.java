package net.simpleAPI.ui.element;

import java.util.Arrays;

/**
 * @author ci010
 */
public class Text extends Element
{
	private String[] src;
	private String text;

	public String[] getSrc() {return src;}

	public String getText() {return text;}

	@Override
	public String toString()
	{
		return "Text{" +
				"src='" + Arrays.toString(src) + '\'' +
				", text='" + text + '\'' +
				"} " + super.toString();
	}
}

package net.simpleAPI.ui.element;

/**
 * @author ci010
 */
public class ProgressBar extends Element
{
	private String bind;

	public String getBind() {return bind;}

	@Override
	public String toString()
	{
		return "ProgressBar{" +
				"bind='" + bind + '\'' +
				"} " + super.toString();
	}
}

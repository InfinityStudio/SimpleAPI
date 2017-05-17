package net.simpleAPI.ui.element;

/**
 * @author ci010
 */
public class Flow extends Group
{
	/**
	 * The direction of the element flow in this pane. Default is horizontal.
	 */
	private String direction = "horizontal";
	/**
	 * If the child should wrap to another line if this line is full.
	 * <p>Or just cutout</p>
	 */
	private boolean wrap = false;
	/**
	 * The space between each element.
	 */
	private int space = 0;

	public boolean isWrap() {return wrap;}

	public void setWrap(boolean wrap)
	{
		this.wrap = wrap;
	}

	public String getDirection() {return direction;}

	public Flow setDirection(String direction)
	{
		this.direction = direction;
		return this;
	}

	public int getSpace() {return space;}

	public Flow setSpace(int space)
	{
		this.space = space;
		return this;
	}

	@Override
	public String toString()
	{
		return "Flow{" +
				"direction='" + direction + '\'' +
				", wrap=" + wrap +
				", space=" + space +
				"} " + super.toString();
	}
}

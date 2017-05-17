package net.simpleAPI.ui.element;

/**
 * @author ci010
 */
public class Slot extends Element
{
	/**
	 * The slot id in {@link net.simpleAPI.inventory.Inventory}.
	 * <p>Should be reachable by {@link net.simpleAPI.inventory.Inventory#getByName(String)}</p>
	 */
	private String bind;

	public String getSlot() {return bind;}

	private int countInRow = 1;

	public int getCountInRow() {return countInRow;}

	@Override
	public String toString()
	{
		return "Slot{" +
				"bind='" + bind + '\'' +
				", countInRow=" + countInRow +
				"} " + super.toString();
	}
}

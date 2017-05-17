package net.simpleAPI.ui.element;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author ci010
 */
public class Element
{
	private String id;
	private Map<String, String> properties = new TreeMap<>();

	/**
	 * @return The custom property of this element.
	 */
	public Map<String, String> getProperties() {return properties;}

	private boolean hidden;
	private boolean disable;

	private Insets insets;

	public Insets getInsets() {return insets;}

	public void setInsets(Insets insets) {this.insets = insets;}

	private int minWidth;
	private int minHeight;

	public int getMinWidth() {return minWidth;}

	public int getMinHeight() {return minHeight;}

	protected final Transform transform = new Transform();

	private ToolTip toolTip;

	public void setId(String id)
	{
		this.id = id;
	}

	public ToolTip getToolTip() {return toolTip;}

	public Element setToolTip(ToolTip toolTip)
	{
		this.toolTip = toolTip;
		return this;
	}

	public String getId()
	{
		return id;
	}

	public Transform transform()
	{
		return transform;
	}

	public boolean isHidden() {return hidden;}

	public void setHidden(boolean hidden) {this.hidden = hidden;}

	public boolean isDisable() {return disable;}

	public void setDisable(boolean disable) {this.disable = disable;}

	public static class ToolTip
	{
		Policy policy;

		void show() {}

		void hide() {}

		enum Policy
		{
			SHOW_ON_HOVER
		}
	}

	public static class Insets
	{
		public int top, down, left, right;

		public Insets(int padding) {top = down = left = right = padding;}

		public Insets(int topDown, int leftRight)
		{
			top = down = topDown;
			left = right = leftRight;
		}

		public Insets(int top, int down, int left, int right)
		{
			this.top = top;
			this.down = down;
			this.left = left;
			this.right = right;
		}
	}

	public static class Transform
	{
		public int x, y, width, height;

		public Transform() {}

		public Transform(int x, int y, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public Transform(Transform transform)
		{
			this.setPos(transform);
			this.setSize(transform);
		}

		public void translate(int x, int y)
		{
			this.x += x;
			this.y += y;
		}

		/**
		 * Set the absolute position of this component.
		 *
		 * @param x The absolute position x of the component.
		 * @param y The absolute position y of the component.
		 * @return this
		 */
		public void setPos(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public void setPos(Transform transform)
		{
			this.x = transform.x;
			this.y = transform.y;
		}

		public void setSize(int width, int height)
		{
			this.height = height;
			this.width = width;
		}

		public void setSize(Transform transform)
		{
			this.width = transform.width;
			this.height = transform.height;
		}

		public boolean containes(int x, int y)
		{
			return x > this.x && x < this.x + this.width &&
					y > this.y && y < this.y + this.height;
		}

		@Override
		public String toString()
		{
			return "[" +
					+x +
					", " + y +
					", " + width +
					", " + height +
					']';
		}
	}

	@Override
	public String toString()
	{
		return "Element{" +
				(id == null ? "" : "id='" + id + '\'') +
				(hidden ? ", hided, " : "") +
				(disable ? ", disabled, " : "") +
				(toolTip == null ? "" : toolTip) +
				", transform=" + transform +
				'}';
	}
}

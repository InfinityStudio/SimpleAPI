package net.simpleAPI.impl.reg;

/**
 * @author ci010
 */
public abstract class RegComponent<T>
{
	private T component;

	public RegComponent(T component)
	{
		this.component = component;
	}

	public T getComponent() {return component;}

	public abstract void register();

	public abstract void registerClient();
}

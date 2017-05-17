package net.simpleAPI.impl.document;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author ci010
 */
public class XMLUtils
{
	static Optional<String> optional(org.w3c.dom.Element element, String attr)
	{
		return element.hasAttribute(attr) ? Optional.ofNullable(element.getAttribute(attr)) : Optional.empty();
	}

	static <T> Optional<T> optional(org.w3c.dom.Element element, String attr, Function<String, T> func)
	{
		try
		{
			return element.hasAttribute(attr) ? Optional.ofNullable(func.apply(element.getAttribute(attr))) : Optional
					.empty();
		}
		catch (Exception e) {return Optional.empty();}
	}
}

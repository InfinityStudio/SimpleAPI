package net.simpleAPI.ui;

import net.simpleAPI.ui.element.Element;

/**
 * @author ci010
 */
public interface ElementParser
{
	Element parse(org.w3c.dom.Element element);
}

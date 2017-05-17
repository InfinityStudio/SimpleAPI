package net.simpleAPI.ui;

import net.simpleAPI.ui.element.Element;

/**
 * @author ci010
 */
public interface ElementDecorator
{
	void decorate(org.w3c.dom.Element domElement, Element element);
}

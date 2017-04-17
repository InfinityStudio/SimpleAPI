package net.simpleAPI.attributes;

/**
 * The {@link #CONSTANTLY} means it will update to client as soon as the value changed. Normally, the value
 * needed to be showed in gui hub({@link net.minecraft.client.gui.GuiIngame}), or the value could affect the
 * common render condition should choose this mode.
 * <p></p>
 * <p>The {@link #LAZY} means it will update to client when the there is a
 * {@link net.minecraft.inventory.Container} pair between client and server. The values that only display on
 * {@link net.minecraft.client.gui.GuiScreen} or special condition should choose this mode.
 */
public enum UpdateMode
{
	CONSTANTLY, LAZY
}

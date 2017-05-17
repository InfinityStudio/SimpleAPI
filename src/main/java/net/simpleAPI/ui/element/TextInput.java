package net.simpleAPI.ui.element;

/**
 * @author ci010
 */
public class TextInput extends Element
{
	private String promptText;

	public String getPromptText() {return promptText;}

	public void setPromptText(String promptText)
	{
		this.promptText = promptText;
	}

	private String text;

	public String getText() {return text;}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return "TextInput{" +
				"promptText='" + promptText + '\'' +
				", text='" + text + '\'' +
				"} " + super.toString();
	}
}

package net.simpleAPI.impl.document;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.simpleAPI.ui.element.*;
import net.simpleAPI.ui.element.Element;
import net.simpleAPI.ui.element.Text;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ci010
 */
public enum DocumentParser
{
	INSTANCE;

	private Map<String, BeanInfo> beanInfoMap = Maps.newTreeMap();
	private static ImmutableMap<String, String> DEFAULT_IMPORT = ImmutableMap.<String, String>builder()
			.put("Flow", Flow.class.getName())
			.put("Group", Group.class.getName())
			.put("Button", Button.class.getName())
			.put("ProgressBar", ProgressBar.class.getName())
			.put("Text", Text.class.getName())
			.put("TextInput", TextInput.class.getName())
			.put("ModelDisplay", ModelDisplay.class.getName())
			.put("Slot", Slot.class.getName())
			.put("Stack", Stack.class.getName())
			.build();

	public Element parse(InputStream stream) throws Exception
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document xmlDoc = documentBuilder.parse(stream);
		return parseElement(xmlDoc.getDocumentElement(), this.parseImport(xmlDoc));
	}

	private Map<String, String> parseImport(org.w3c.dom.Document doc)
	{
		Map<String, String> importClass = DEFAULT_IMPORT;
		NodeList childNodes = doc.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
			{
				ProcessingInstruction instruction = (ProcessingInstruction) item;
				if (instruction.getTarget().equals("import"))
				{
					String fullName = instruction.getData();
					Preconditions.checkArgument(StringUtils.isNoneEmpty(fullName));
					String simpleName = fullName.substring(fullName.lastIndexOf('.'));
					Preconditions.checkArgument(StringUtils.isNoneEmpty(simpleName));
					if (importClass == DEFAULT_IMPORT)
					{
						importClass = Maps.newTreeMap();
						importClass.putAll(DEFAULT_IMPORT);
					}
					if (importClass.containsKey(simpleName)) throw new IllegalArgumentException();
					importClass.put(simpleName, fullName);
				}
			}
		}
		return importClass;
	}

	private Element parseElement(org.w3c.dom.Element element, Map<String, String> importClass) throws Exception
	{
		Objects.requireNonNull(element);

		Element construct = construct(element.getTagName(), importClass);
		decorate(element, construct);

		NodeList childNodes = element.getChildNodes();
		if (childNodes.getLength() == 0) return construct;
		if (construct instanceof Group)
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE)
					((Group) construct).getChildren().add(parseElement((org.w3c.dom.Element) item, importClass));
			}
		return construct;
	}

	private Element construct(String tagName, Map<String, String> importClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		String name = importClass.getOrDefault(tagName, tagName);
		Class<?> aClass = Class.forName(name);
		if (!Element.class.isAssignableFrom(aClass)) throw new IllegalArgumentException("Unknown tag name: " + tagName);
		return (Element) aClass.newInstance();
	}

	private void decorate(org.w3c.dom.Element element, Element out)
	{
		Class<?> aClass = out.getClass();
		if (Element.class.isAssignableFrom(aClass))
		{
			NamedNodeMap attributes = element.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++)
				if (!initialize(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue(), out))
				{
					//TODO log
				}
		}
		else throw new IllegalArgumentException();
	}


	private BeanInfo getBeanInfo(Class<?> c)
	{
		String name = c.getName();
		BeanInfo beanInfo = beanInfoMap.get(name);
		if (beanInfo == null)
			beanInfoMap.put(name, beanInfo = new BeanInfo(c));
		return beanInfo;
	}

	boolean initialize(String attr, String value, Object inst)
	{
		Class<?> clz = inst.getClass();
		BeanInfo beanInfo = getBeanInfo(clz);
		FieldInfo info = beanInfo.getInfo(attr);
		while (info == null)
		{
			clz = clz.getSuperclass();
			if (clz == Object.class) break;
			beanInfo = getBeanInfo(clz);
			info = beanInfo.getInfo(attr);
		}
		if (info == null) return false;
		info.set(inst, value);
		return true;
	}

	class BeanInfo
	{
		Class<?> clz;
		Map<String, FieldInfo> fieldMap = Maps.newTreeMap();

		BeanInfo(Class<?> clz) {this.clz = clz;}

		private FieldInfo getInfo(String fieldName)
		{
			if (!fieldMap.containsKey(fieldName))
			{
				try
				{
					fieldMap.put(fieldName, new FieldInfo(clz.getDeclaredField(fieldName)));
				}
				catch (Exception e)
				{
					fieldMap.put(fieldName, null);
				}
			}
			return fieldMap.get(fieldName);
		}
	}

	private static ImmutableMap<Class, Function<String, Object>> TRANSLATOR = ImmutableMap.<Class, Function<String, Object>>builder()
			.put(int.class, Integer::parseInt)
			.put(float.class, Float::parseFloat)
			.put(long.class, Long::parseLong)
			.put(short.class, Short::parseShort)
			.put(char.class, s -> s.charAt(0))
			.put(double.class, Double::parseDouble)
			.put(boolean.class, Boolean::parseBoolean)
			.put(String.class, s -> s)
			.put(String[].class, s -> s.split(" "))
			.put(ResourceLocation.class, ResourceLocation::new)
			.put(Element.Insets.class, s ->
			{
				String[] split = s.split(" ");
				if (split.length == 1)
					return new Element.Insets(Integer.valueOf(split[0]));
				if (split.length == 2)
					return new Element.Insets(Integer.valueOf(split[0]),
							Integer.valueOf(split[1]));
				if (split.length == 3)
					return new Element.Insets(Integer.valueOf(split[0]),
							Integer.valueOf(split[1]),
							Integer.valueOf(split[2]),
							Integer.valueOf(split[2]));
				if (split.length == 4)
					return new Element.Insets(Integer.valueOf(split[0]),
							Integer.valueOf(split[1]),
							Integer.valueOf(split[2]),
							Integer.valueOf(split[3]));
				throw new IllegalArgumentException("Cannot parse the Insets: " + s);
			})
			.build();

	class FieldInfo
	{
		private Field field;
		private Function<String, Object> translate;

		FieldInfo(Field declaredField)
		{
			this.field = declaredField;
			if (!field.isAccessible()) field.setAccessible(true);
			Class<?> type = declaredField.getType();
			if (TRANSLATOR.containsKey(type))
				translate = TRANSLATOR.get(type);
			else if (type.isEnum())
			{
				final Class<? extends Enum> eClass = (Class<? extends Enum>) type;
				translate = (s) -> Enum.valueOf(eClass, s.toUpperCase());
			}
			else throw new IllegalArgumentException("Unsupported element attribute type: " + type);
		}

		void set(Object obj, String value)
		{
			try
			{
				field.set(obj, translate.apply(value));
			}
			catch (IllegalAccessException e)
			{
				throw new Error(e);//this should not happened
			}
			catch (Exception other)
			{
				throw new Error("Error during parsing. Might be syntax error!", other);
			}
		}
	}
}

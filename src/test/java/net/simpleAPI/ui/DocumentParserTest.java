package net.simpleAPI.ui;

import net.simpleAPI.impl.document.DocumentParser;
import net.simpleAPI.ui.element.Element;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @author ci010
 */
public class DocumentParserTest
{
	@Test
	public void testRead() throws Exception
	{
		InputStream stream = DocumentParserTest.class.getResourceAsStream("/gui-sample.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document xmlDoc = documentBuilder.parse(stream);
		NamedNodeMap attributes = xmlDoc.getDocumentElement().getAttributes();
		System.out.println(attributes.getLength());
	}

	@Test
	public void test() throws Exception
	{
		InputStream stream = DocumentParserTest.class.getResourceAsStream("/gui-sample.xml");

		DocumentParser documentParser = DocumentParser.INSTANCE;
		Element parse = documentParser.parse(stream);
		IOUtils.closeQuietly(stream);
		System.out.println(parse);
	}
}

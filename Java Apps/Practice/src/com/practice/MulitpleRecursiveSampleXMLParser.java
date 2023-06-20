import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class MulitpleRecursiveSampleXMLParser {
	public static void main(String[] args) {
		List<String> tags = Arrays.asList("results.inheritedAdSenseSettings.value.adSenseEnabled", "results.id");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			tags.forEach(tag -> parse(tag, saxParser));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parse(String tag, SAXParser saxParser) {
		String tagsList[] = tag.split("\\.");
		try {
			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");

			DefaultHandler handler = new DefaultHandler() {
				boolean tagFlag[] = new boolean[tagsList.length];
				String lastName;

				public void startElement(String uri, String localName, String qName, Attributes attributes) {
					for (int i = 0; i < tagsList.length; i++) {
						int colonIndex = qName.indexOf(":");
						String qNameWoNamespace = colonIndex != -1 ? qName.substring(colonIndex + 1) : qName;
						if (qNameWoNamespace.equalsIgnoreCase(tagsList[i])) {
							if (i == 0)
								tagFlag[0] = true;
							else if (tagFlag[i - 1]) {
								tagFlag[i] = true;
								lastName = qName;
							}
						}
					}
				}

				public void endElement(String uri, String localName, String qName) {
					for (int i = 0; i < tagsList.length; i++) {
						int colonIndex = qName.indexOf(":");
						String qNameWoNamespace = colonIndex != -1 ? qName.substring(colonIndex) : qName;
						if (qNameWoNamespace.equalsIgnoreCase(tagsList[i])) {
							if (i == 0)
								tagFlag[0] = false;
							else if (tagFlag[i - 1])
								tagFlag[i] = false;
						}
					}
				}

				public void characters(char ch[], int start, int length) {
					if (tagFlag[tagFlag.length - 1]) {
						System.out.println("Nested Element Value " + lastName + ": " + new String(ch, start, length));
					}
				}
			};

			saxParser.parse(inputFile, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

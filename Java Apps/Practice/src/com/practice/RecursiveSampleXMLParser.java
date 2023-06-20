import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RecursiveSampleXMLParser {
	public static void main(String[] args) {
		String tag = "results.inheritedAdSenseSettings.value.adSenseEnabled";
		String tagsList[] = tag.split("\\.");
		try {
			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				boolean tagFlag[] = new boolean[tagsList.length];

				public void startElement(String uri, String localName, String qName, Attributes attributes) {
					for (int i = 0; i < tagsList.length; i++) {
						if (qName.equalsIgnoreCase(tagsList[i])) {
							if (i == 0)
								tagFlag[0] = true;
							else if (tagFlag[i - 1])
								tagFlag[i] = true;
						}
					}
				}

				public void endElement(String uri, String localName, String qName) {
					for (int i = 0; i < tagsList.length; i++) {
						if (qName.equalsIgnoreCase(tagsList[i])) {
							if (i == 0)
								tagFlag[0] = false;
							else if (tagFlag[i - 1])
								tagFlag[i] = false;
						}
					}
				}

				public void characters(char ch[], int start, int length) {
					if (tagFlag[tagFlag.length - 1]) {
						System.out.println("Nested Element Value: " + new String(ch, start, length));
					}
				}
			};

			saxParser.parse(inputFile, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

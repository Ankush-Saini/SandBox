import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RecursiveSampleXMLParser {
	public static void main(String[] args) {
		List<String> tags = Arrays.asList("results.inheritedAdSenseSettings.value.adSenseEnabled", "results.id{-2}");

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
		int splitHolderValue = -1;
		int startInd = tagsList[tagsList.length - 1].indexOf("{");
		if (startInd != -1) {
			int endInd = tagsList[tagsList.length - 1].indexOf("}");
			String finalLastTag = tagsList[tagsList.length - 1].substring(0, startInd);
			String splitString = tagsList[tagsList.length - 1].substring(startInd + 1, endInd);
			tagsList[tagsList.length - 1] = finalLastTag;
			splitHolderValue = Integer.valueOf(splitString);
		}
		final boolean isSplitIndex = startInd != -1;
		final int splitIndex = splitHolderValue;
		try {
			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");

			DefaultHandler handler = new DefaultHandler() {
				boolean tagFlag[] = new boolean[tagsList.length];
				String lastName;
				List<String> matchingValues = new ArrayList<>();

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
						matchingValues.add(new String(ch, start, length));
					}
				}

				@Override
				public void endDocument() throws SAXException {
					matchingValues.forEach(value -> {
						System.out.println(lastName + " " + replaceStringPlaceholder(value));
					});
				}

				private String replaceStringPlaceholder(String originalValue) {
					if (!isSplitIndex)
						return originalValue;
					else {
						if (splitIndex >= 0)
							return "$" + originalValue.substring(0, splitIndex) + "$"
									+ originalValue.substring(splitIndex);
						else {
							int finalInd = originalValue.length() + splitIndex;
							return originalValue.substring(0, finalInd) + "$" + originalValue.substring(finalInd) + "$";
						}
					}
				}
			};

			saxParser.parse(inputFile, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

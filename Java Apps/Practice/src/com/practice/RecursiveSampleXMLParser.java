import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RecursiveSampleXMLParser {
	public static void main(String[] args) {
		List<String> tags = Arrays.asList("results.inheritedAdSenseSettings.value.adSenseEnabled", "results.id{-2}");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			final String fileContent = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
					+ "  <soap:Header>\r\n"
					+ "    <ResponseHeader xmlns=\"https://www.google.com/apis/ads/publisher/v202305\">\r\n"
					+ "      <requestId>xxxxxxxxxxxxxxxxxxxx</requestId>\r\n"
					+ "      <responseTime>1063</responseTime>\r\n" + "    </ResponseHeader>\r\n"
					+ "  </soap:Header>\r\n" + "  <soap:Body>\r\n"
					+ "    <getAdUnitsByStatementResponse xmlns=\"https://www.google.com/apis/ads/publisher/v202305\">\r\n"
					+ "      <rval>\r\n" + "        <totalResultSetSize>2</totalResultSetSize>\r\n"
					+ "        <startIndex>0</startIndex>\r\n" + "        <results>\r\n" + "          <id>2371</id>\r\n"
					+ "          <name>RootAdUnit</name>\r\n" + "          <description></description>\r\n"
					+ "          <targetWindow>TOP</targetWindow>\r\n" + "          <status>ACTIVE</status>\r\n"
					+ "          <adUnitCode>1002372</adUnitCode>\r\n" + "          <inheritedAdSenseSettings>\r\n"
					+ "            <value>\r\n" + "              <adSenseEnabled>true</adSenseEnabled>\r\n"
					+ "              <borderColor>FFFFFF</borderColor>\r\n"
					+ "              <titleColor>0000FF</titleColor>\r\n"
					+ "              <backgroundColor>FFFFFF</backgroundColor>\r\n"
					+ "              <textColor>000720</textColor>\r\n"
					+ "              <urlColor>008000</urlColor>\r\n"
					+ "              <adType>TEXT_AND_IMAGE</adType>\r\n"
					+ "              <borderStyle>DEFAULT</borderStyle>\r\n"
					+ "              <fontFamily>DEFAULT</fontFamily>\r\n"
					+ "              <fontSize>DEFAULT</fontSize>\r\n" + "            </value>\r\n"
					+ "          </inheritedAdSenseSettings>\r\n" + "        </results>\r\n" + "	<results>\r\n"
					+ "          <id>2372</id>\r\n" + "          <name>RootAdUnit</name>\r\n"
					+ "          <description></description>\r\n" + "          <targetWindow>TOP</targetWindow>\r\n"
					+ "          <status>ACTIVE</status>\r\n" + "          <adUnitCode>1002372</adUnitCode>\r\n"
					+ "          <inheritedAdSenseSettings>\r\n" + "            <value>\r\n"
					+ "              <adSenseEnabled>true</adSenseEnabled>\r\n"
					+ "              <borderColor>FFFFFF</borderColor>\r\n"
					+ "              <titleColor>0000FF</titleColor>\r\n"
					+ "              <backgroundColor>FFFFFF</backgroundColor>\r\n"
					+ "              <textColor>000000</textColor>\r\n"
					+ "              <urlColor>008000</urlColor>\r\n"
					+ "              <adType>TEXT_AND_IMAGE</adType>\r\n"
					+ "              <borderStyle>DEFAULT</borderStyle>\r\n"
					+ "              <fontFamily>DEFAULT</fontFamily>\r\n"
					+ "              <fontSize>DEFAULT</fontSize>\r\n" + "            </value>\r\n"
					+ "          </inheritedAdSenseSettings>\r\n" + "        </results>\r\n" + "      </rval>\r\n"
					+ "    </getAdUnitsByStatementResponse>\r\n" + "  </soap:Body>\r\n" + "</soap:Envelope>";

			Map<String, String> finalValueMap = new HashMap<>();
			tags.forEach(tag -> parse(tag, saxParser, fileContent, finalValueMap));
			String modifiedContent = new String(fileContent);
			for (Map.Entry<String, String> mapEntry : finalValueMap.entrySet()) {
				modifiedContent = modifiedContent.replace(mapEntry.getKey(), mapEntry.getValue());
			}
			System.out.println(modifiedContent);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parse(String tag, SAXParser saxParser, String fileContent, Map<String, String> finalValueMap) {
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
//			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");
			InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
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
						String finalValue = replaceStringPlaceholder(value);
						System.out.println(lastName + " " + finalValue);
						finalValueMap.put(createTag(value, lastName), createTag(finalValue, lastName));
					});
				}

				private String replaceStringPlaceholder(String originalValue) {
					if (!isSplitIndex)
						return originalValue;
					else {
						if (splitIndex >= 0)
							return originalValue.substring(0, splitIndex) + "$" + originalValue.substring(splitIndex)
									+ "$";
						else {
							int finalInd = originalValue.length() + splitIndex;
							return "$" + originalValue.substring(0, finalInd) + "$" + originalValue.substring(finalInd);
						}
					}
				}

				private String createTag(String value, String tagName) {
					return "<" + tagName + ">" + value + "" + "</" + tagName + ">";
				}
			};

			saxParser.parse(inputStream, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

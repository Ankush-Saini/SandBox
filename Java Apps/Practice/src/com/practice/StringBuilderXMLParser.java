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

public class StringBuilderXMLParser {
	public static void main(String[] args) {
		List<String> tags = Arrays.asList("net_areas.net_Area.net_area",
				"results.inheritedAdSenseSettings.value.adSenseEnabled", "results.adUnitCode");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			final String fileContent = "<env:Envelope\r\n"
					+ "	xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + "	<env:Header/>\r\n"
					+ "	<env:Body>\r\n" + "		<m:getWorkAreasResponse\r\n"
					+ "			xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\r\n"
					+ "			xmlns:m=\"http://netg.z.com/WS/AService/\"\r\n"
					+ "			xmlns:ns3=\"http://netgeo.vzw.com/WS/Common/\">\r\n" + "			<m:net_areas>\r\n"
					+ "				<m:net_Area>\r\n" + "					<m:net_area>west</m:net_area>\r\n"
					+ "					<m:state>\r\n" + "						<m:net_area>north</m:net_area>\r\n"
					+ "					</m:state>\r\n" + "				</m:net_Area>\r\n"
					+ "				<m:net_Area>\r\n" + "					<m:net_area>east</m:net_area>\r\n"
					+ "				</m:net_Area>\r\n" + "				<m:net_area>west1</m:net_area>\r\n"
					+ "			</m:net_areas>\r\n" + "			<m:net_area>south</m:net_area>\r\n"
					+ "		</m:getWorkAreasResponse>\r\n" + "	</env:Body>\r\n" + "</env:Envelope>";

			Map<String, String> finalValueMap = new HashMap<>();
//			saxParser.parse(new ByteArrayInputStream(fileContent.getBytes()), myXMLParser);
			tags.forEach(tag -> parse(tag, saxParser, fileContent, finalValueMap));
//			String modifiedContent = new String(fileContent);
//			for (Map.Entry<String, String> mapEntry : finalValueMap.entrySet()) {
//				modifiedContent = modifiedContent.replace(mapEntry.getKey(), mapEntry.getValue());
//			}
//			System.out.println(modifiedContent);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
				String lastName = "";
				List<String> matchingValues = new ArrayList<>();
				private StringBuilder dataBuffer = new StringBuilder();
				private int currentLevel = 0;

				private String getQualifiedNameWithoutNameSpace(String qName) {
					int colonIndex = qName.indexOf(":");
					return colonIndex != -1 ? qName.substring(colonIndex+1) : qName;
				}

				public void startElement(String uri, String localName, String qName, Attributes attributes) {
					String qNameWithoutNameSpace = getQualifiedNameWithoutNameSpace(qName);
					if (qNameWithoutNameSpace.equals(tagsList[0])) {
						dataBuffer.append(qNameWithoutNameSpace);
					} else if (dataBuffer.length() != 0) {
						dataBuffer.append("." + qNameWithoutNameSpace);
					}
					currentLevel++;
//					System.out.println("Tag: " + qName + " Push: " + currentLevel + dataBuffer.toString());
				}

				public void endElement(String uri, String localName, String qName) {
					int dotIndex = dataBuffer.lastIndexOf(".") == -1 ? 0 : dataBuffer.lastIndexOf(".");
					dataBuffer.replace(dotIndex, dataBuffer.length(), "");
//					System.out.println("Tag: " + qName + " Pop: " + currentLevel + dataBuffer.toString());
					currentLevel--;
				}

				public void characters(char ch[], int start, int length) {
//					System.out.println(dataBuffer.toString());
					if (dataBuffer.toString().equals(tag)) {
						lastName = tagsList[tagsList.length - 1];
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
					if (!isSplitIndex || originalValue.length() <= Math.abs(splitIndex))
						return "$$[" + originalValue + "]$$";
					else {
						if (splitIndex >= 0)
							return originalValue.substring(0, splitIndex) + "$$[" + originalValue.substring(splitIndex)
									+ "]$$";
						else {
							int finalInd = originalValue.length() + splitIndex;
							return "$$[" + originalValue.substring(0, finalInd) + "]$$"
									+ originalValue.substring(finalInd);
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

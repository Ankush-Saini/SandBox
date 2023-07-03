import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

public class XMLParserV3 {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List<String> tags = Arrays.asList("id{-2}", "name");

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
					+ "          </inheritedAdSenseSettings>\r\n" + "        </results>\r\n"
					+ "<customer><id>45443</id></customer>	<results>\r\n" + "          <id>2372</id>\r\n"
					+ "          <name>RootAdUnit</name>\r\n" + "          <description></description>\r\n"
					+ "          <targetWindow>TOP</targetWindow>\r\n" + "          <status>ACTIVE</status>\r\n"
					+ "          <adUnitCode>1002372</adUnitCode>\r\n" + "          <inheritedAdSenseSettings>\r\n"
					+ "            <value>\r\n" + "              <adSenseEnabled>true</adSenseEnabled>\r\n"
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

			parse(tags, saxParser, fileContent);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();

		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
	}

	private static void parse(List<String> originalTagList, SAXParser saxParser, String fileContent) {
		Map<String, Integer> tagListToSearch = createTagMapToSearch(originalTagList);
		try {
//			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");
			InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
			DefaultHandler handler = new DefaultHandler() {
				boolean tagFlag = false;
				String tagName = "";
				Integer escapeIndex = Integer.valueOf(0);
				int beginIndex = 0;
				int endIndex = 0;
				StringBuilder sb = new StringBuilder();
				char charArr[] = null;

				public String getModifiedContent() {
					return sb.toString();
				}

				public void startElement(String uri, String localName, String qName, Attributes attributes) {
					tagFlag = false;
					int colonIndex = qName.indexOf(":");
					String qNameWoNamespace = colonIndex != -1 ? qName.substring(colonIndex + 1) : qName;
					if (tagListToSearch.containsKey(qNameWoNamespace)) {
						tagFlag = true;
						tagName = qName;
						escapeIndex = tagListToSearch.get(qNameWoNamespace);
					}
				}

				public void endElement(String uri, String localName, String qName) {
					int colonIndex = qName.indexOf(":");
					String qNameWoNamespace = colonIndex != -1 ? qName.substring(colonIndex + 1) : qName;
					if (tagListToSearch.containsKey(qNameWoNamespace)) {
						tagFlag = false;
					}
				}

				public void characters(char ch[], int start, int length) {
					if (charArr == null)
						charArr = ch;
					endIndex = start;
					sb.append(new String(ch, beginIndex, endIndex - beginIndex));
					if (tagFlag) {
//						System.out.println(tagName + " " + tagValue + " " + start + " " + length);
						String finalValue = replaceStringPlaceholder(new String(ch, start, length), escapeIndex);
						sb.append(finalValue);
					} else {
						sb.append(new String(ch, start, length));
					}
					beginIndex = start + length;
				}

				@Override
				public void endDocument() throws SAXException {
					sb.append(new String(charArr, beginIndex, charArr.length - beginIndex));
					System.out.println(sb.toString());
				}

				private String replaceStringPlaceholder(String originalValue, Integer escapeIndex) {
					if (escapeIndex == 0 || originalValue.length() <= Math.abs(escapeIndex))
						return "$$[" + originalValue + "]$$";
					else {
						if (escapeIndex >= 0)
							return originalValue.substring(0, escapeIndex) + "$$["
									+ originalValue.substring(escapeIndex) + "]$$";
						else {
							int finalInd = originalValue.length() + escapeIndex;
							return "$$[" + originalValue.substring(0, finalInd) + "]$$"
									+ originalValue.substring(finalInd);
						}
					}
				}

			};

			saxParser.parse(inputStream, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Integer> createTagMapToSearch(List<String> originalTagList) {
		Map<String, Integer> map = new HashMap<>();
		for (String originalTag : originalTagList) {
			String computedTagName = originalTag;
			Integer escapeIndex = Integer.valueOf(0);
			int startInd = originalTag.indexOf("{");
			if (startInd != -1) {
				int endInd = originalTag.indexOf("}");
				String splitString = originalTag.substring(startInd + 1, endInd);
				escapeIndex = Integer.valueOf(splitString);
				computedTagName = originalTag.substring(0, startInd);
			}
			map.put(computedTagName, escapeIndex);
		}
		return map;
	}
}
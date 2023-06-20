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
		List<String[]> tagsList = convertTags(tags);
		try {
			File inputFile = new File("D:\\SandBox\\Java Apps\\sample_soap.xml.txt");

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = getCustomHandler(tagsList);

			saxParser.parse(inputFile, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String[]> convertTags(List<String> tags) {
		List<String[]> tagList = new ArrayList<>();
		tags.forEach(tag -> {
			tagList.add(tag.split("\\."));
		});
		return tagList;
	}

	private static DefaultHandler getCustomHandler(List<String[]> tagList) {
		return new DefaultHandler() {
			List<boolean[]> flags = getArrayList();
			List<String> lastQNames = new ArrayList();

			public List<boolean[]> getArrayList() {
				List<boolean[]> flags = new ArrayList<>();
				for (int i = 0; i < tagList.size(); i++) {
					boolean[] flag = new boolean[tagList.get(i).length];
					flags.add(flag);
				}
				return flags;
			}

			private String getTagName(String qName) {
				int colonIndex = qName.indexOf(":");
				return colonIndex != -1 ? qName.substring(colonIndex + 1) : qName;
			}

			public void startElement(String uri, String localName, String qName, Attributes attributes) {
				for (int index = 0; index < tagList.size(); index++) {
					boolean tagFlag[] = flags.get(index);
					String[] tags = tagList.get(index);
					for (int i = 0; i < tags.length; i++) {
						String qNameWoNamespace = getTagName(qName);
						if (qNameWoNamespace.equalsIgnoreCase(tags[i])) {
							if (i == 0)
								tagFlag[0] = true;
							else if (tagFlag[i - 1]) {
								tagFlag[i] = true;
								lastQNames.add(qName);
							}
						}
					}
				}
			}

			public void endElement(String uri, String localName, String qName) {
				for (int index = 0; index < tagList.size(); index++) {
					boolean tagFlag[] = flags.get(index);
					String[] tags = tagList.get(index);
					for (int i = 0; i < tags.length; i++) {
						String qNameWoNamespace = getTagName(qName);
						if (qNameWoNamespace.equalsIgnoreCase(tags[i])) {
							if (i == 0)
								tagFlag[0] = false;
							else if (tagFlag[i - 1]) {
								tagFlag[i] = false;
							}
						}
					}
				}
			}

			public void characters(char ch[], int start, int length) {
				System.out.println(flags.size() + " " + lastQNames.size());
				for (int i = 0; i < flags.size(); i++) {
					boolean[] tagFlag = flags.get(i);
					if (tagFlag[tagFlag.length - 1]) {
						System.out.println("QName: " + lastQNames.get(0) + " " + new String(ch, start, length));
					}
				}
			}
		};
	}
}

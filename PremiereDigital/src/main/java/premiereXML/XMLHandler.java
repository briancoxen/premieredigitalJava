package premiereXML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;


public class XMLHandler {
	private File file;
	private Document doc;

	public XMLHandler(File file) {
		this.file = file;
	}
	
	public void parseXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
		this.doc = dBuilder.parse(file);
	}
	
	public String loadXMLData() {
		String json = new String();
		for (int count = 0; count < doc.getChildNodes().getLength(); count++) {
			Node movies = doc.getChildNodes().item(count);
			Element eMovies = (Element) movies;
			json = "{\"title\":\"" + eMovies.getElementsByTagName("Title").item(0).getTextContent() + "\"}";
		}
		
		return json;
	}
}

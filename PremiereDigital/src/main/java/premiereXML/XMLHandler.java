package premiereXML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import DB.PremiereDBConn;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class XMLHandler {
	private File file;
	private Document doc;

	public XMLHandler(File file) {
		this.file = file;
	}

	public void parseXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.doc = dBuilder.parse(file);
	}

	public String loadXMLData() throws SQLException {
		NodeList nList = doc.getElementsByTagName("movie");
		for (int count = 0; count < nList.getLength(); count++) {
			PremiereDBConn mySQL = new PremiereDBConn();
			Connection myDB = null;
			Statement stmt = null;

			try {
				Node movies = nList.item(count);
				Element eMovies = (Element) movies;

				NodeList meta = eMovies.getElementsByTagName("Meta").item(0).getChildNodes();
				Element eMeta = (Element) meta;

				String update = String.format(
						"INSERT into movies (Title, MD5, Director, ReleaseDate, Description, Length, Type) values ('%s','%s','%s','%s','%s','%s','%s')",
						eMovies.getElementsByTagName("Title").item(0).getTextContent(),
						eMovies.getElementsByTagName("MD5").item(0).getTextContent(), 
						eMeta.getElementsByTagName("Director").item(0).getTextContent(), 
						eMeta.getElementsByTagName("Released").item(0).getTextContent(),
						eMeta.getElementsByTagName("Description").item(0).getTextContent(),
						eMeta.getElementsByTagName("Length").item(0).getTextContent(), 
						eMeta.getElementsByTagName("Type").item(0).getTextContent());

				myDB = (Connection) mySQL.getDB();
				stmt = (Statement) myDB.createStatement();
				stmt.executeUpdate(update);

			} catch (Exception e) {
				return "{\"error\":\"" + e + "\"}";
			} finally {
				myDB.close();
				stmt.close();
			}
		}
		return "{\"success\":\"0\"}";
	}
}

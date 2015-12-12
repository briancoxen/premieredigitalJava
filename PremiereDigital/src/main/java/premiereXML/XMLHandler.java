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
import java.sql.ResultSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	
	@SuppressWarnings("unchecked")
	public String loadXMLData() {
		NodeList nList = doc.getElementsByTagName("movie");
		JSONArray response = new JSONArray();
		for (int count = 0; count < nList.getLength(); count++) {
			Node movies = nList.item(count);
			Element eMovies = (Element) movies;
			String title = eMovies.getElementsByTagName("Title").item(0).getTextContent();
			String md5 = eMovies.getElementsByTagName("MD5").item(0).getTextContent();
			String meta = eMovies.getElementsByTagName("Meta").item(0).getTextContent();
			
			StringBuffer sb = new StringBuffer();
            sb.append("{");
            
            sb.append(JSONObject.escape("title"));
            sb.append(":");
            sb.append("\"" + JSONObject.escape(title) + "\"");
            
            sb.append(",");
            
            sb.append(JSONObject.escape("MD5"));
            sb.append(":");
            sb.append("\"" + JSONObject.escape(md5) + "\"");
            
            sb.append(",");
            
            sb.append(JSONObject.escape("Meta"));
            sb.append(":");
            sb.append("\"" + JSONObject.escape(meta) + "\"");
            
            sb.append("}");
           
			response.add(sb.toString());
			
		    PremiereDBConn mySQL = new PremiereDBConn();
		    String update = String.format("INSERT into movies values (%s,%s,%s)", title, meta, md5);
		    
		    try {
		    	Connection myDB = (Connection) mySQL.getDB();
		    	Statement stmt = (Statement) myDB.createStatement();
		    	int rs = stmt.executeUpdate(update);
		    	
		    } catch(Exception e) {
		    	return "{\"error\":\""+e+"\"}";
		    }
		    
		}
		return response.toJSONString();
	}
}

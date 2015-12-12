package com.premiere;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import DB.PremiereDBConn;

@Controller
public class GetMovieData {
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins="http://52.25.116.171")
    @RequestMapping(value="/getMovieData", method=RequestMethod.GET)
    public @ResponseBody String getMovieData(){
		JSONArray response = new JSONArray();
		PremiereDBConn mySQL = new PremiereDBConn();
	    String query = "SELECT * from movies";
	    
	    try {
	    	Connection myDB = (Connection) mySQL.getDB();
	    	Statement stmt = (Statement) myDB.createStatement();
	    	ResultSet rs = stmt.executeQuery(query);
	    	
	    	try {
				if (rs.isBeforeFirst()) {
					while (rs.next()) {
						StringBuffer sb = new StringBuffer();
			            sb.append("{");
			            
			            sb.append(JSONObject.escape("title"));
			            sb.append(":");
			            sb.append("\"" + JSONObject.escape(rs.getString("Title")) + "\"");
			            
			            sb.append(",");
			            
			            sb.append(JSONObject.escape("MD5"));
			            sb.append(":");
			            sb.append("\"" + JSONObject.escape(rs.getString("MD5")) + "\"");
			            
			            sb.append(",");
			            
			            sb.append(JSONObject.escape("Meta"));
			            sb.append(":");
			            sb.append("\"" + JSONObject.escape(rs.getString("Meta")) + "\"");
			            
			            sb.append("}");
			           
						response.add(sb.toString());
					}
				}
	    	} catch(SQLException sql) {
	    		return "{\"error\":\""+sql+"\"}";
	    	}
	    	
	    } catch(Exception e) {
	    	return "{\"error\":\""+e+"\"}";
	    }
        return response.toJSONString();
    }
}

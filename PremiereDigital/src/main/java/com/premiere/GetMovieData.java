package com.premiere;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.*;
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
		PremiereDBConn mySQL = new PremiereDBConn();
	    String query = "SELECT * from movies";
	    JSONObject root = new JSONObject();
	    try {
	    	Connection myDB = (Connection) mySQL.getDB();
	    	Statement stmt = (Statement) myDB.createStatement();
	    	ResultSet rs = stmt.executeQuery(query);
	    	int count = 0;
	    	try {
				if (rs.isBeforeFirst()) {
					while (rs.next()) {
						JSONObject child = new JSONObject();
						root.put(count, child);
						child.put("Title", rs.getString("Title"));
			            child.put("MD5", rs.getString("MD5"));
			            child.put("Meta", rs.getString("Meta"));
			            count++;
					}
				}
	    	} catch(SQLException sql) {
	    		return "{\"error\":\""+sql+"\"}";
	    	}
	    	
	    } catch(Exception e) {
	    	return "{\"error\":\""+e+"\"}";
	    }
        return root.toJSONString();
    }
}

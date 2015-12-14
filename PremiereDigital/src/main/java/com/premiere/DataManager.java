package com.premiere;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import DB.PremiereDBConn;

@Controller
public class DataManager {
	@CrossOrigin(origins="http://ec2-52-25-116-171.us-west-2.compute.amazonaws.com")
    @RequestMapping(value="/deleteFile", method=RequestMethod.POST)
    public @ResponseBody String deleteFile(@RequestParam("id") String id) throws SQLException{
		PremiereDBConn mySQL = new PremiereDBConn();
		Connection myDB = null;
		Statement stmt = null;
		
		String setNull = String.format("UPDATE movies set File= NULL where id=%s", id);
		
		try {
			myDB = (Connection) mySQL.getDB();
	    	stmt = (Statement) myDB.createStatement();
	    	stmt.executeUpdate(setNull);
	    	
	    } catch(SQLException sql) {
	    	return "{\"error\":\""+sql+"\"}";
	    } finally {
			myDB.close();
			stmt.close();
	    }
		return "{\"success\":\"0\"}";
    }
}


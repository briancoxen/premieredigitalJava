package DB;

import java.sql.*;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class PremiereDBConn {
	private final String user = "premiereAdmin";
	private final String pass = "premiere";
	
	public PremiereDBConn() {
		
	}
	
	public Connection getDB() throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();
		Connection conn = null;

		try {
			dataSource.setUser(user);
			dataSource.setPassword(pass);
			dataSource.setServerName("localhost");
			dataSource.setPort(3306);
		    dataSource.setDatabaseName("premiere");
		    conn = dataSource.getConnection();
		    	
		} catch (SQLException se) {
			
		}
		return conn;
	}
}

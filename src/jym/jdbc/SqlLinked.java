// CatfoOD 2009-9-16 ÏÂÎç01:14:38 
package jym.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlLinked {
	private Connection conn;
	private Statement state;
	private String dbtype;
	private String linkname;
	private ConnectConfig conf;
	
	public SqlLinked(ConnectConfig conf, String name) throws SQLException, ClassNotFoundException {
		dbtype = conf.getDbtype();
		linkname = name;
		DBServer server = DBFactory.get( dbtype );
		
		if (server==null) {
			throw new SQLException("dbtype invalidation.");
		}
		
		server.registerDriver();
		
		String url = server.getUrl(conf);
		
		conn = DriverManager.getConnection( url, conf.getUsername(), conf.getPassword() );
		state = conn.createStatement();
		this.conf = conf;
	}
	
	public ResultSet exe(String sql) throws SQLException {
		return state.executeQuery(sql);
	}
	
	public Statement getStatement() {
		return state;
	}
	
	public String getDBtype() {
		return dbtype;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void close() {
		try {
			state.close();
			conn.close();
		} catch (Exception e) {}
	}
	
	public String getName() {
		return linkname;
	}
	
	public ConnectConfig getConfig() {
		return conf;
	}
	
	public static void close(ResultSet rs) {
		if (rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Statement s) {
		if (s!=null) {
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

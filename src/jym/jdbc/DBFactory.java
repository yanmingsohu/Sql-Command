// CatfoOD 2009-9-16 下午03:50:11 
package jym.jdbc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DBFactory {
	private static Map<String, DBServer> servers =
		new HashMap<String, DBServer>();
	
	static {
		init();
	}
	
	/**
	 * %1$s - 服务器地址
	 * %2$s - 端口
	 * %3$s - 实例名字
	 */
	private static void init() {
		
		DBServer ora = new DBServer(
				"jdbc:oracle:thin:@%1$s:%2$s:%3$s", 
				"oracle.jdbc.driver.OracleDriver" );
		
		DBServer my = new DBServer(
				"jdbc:mysql://%1$s:%2$s/%3$s", 
				"com.mysql.jdbc.Driver" );
		
		DBServer ms = new DBServer(
				"jdbc:sqlserver://%1$s:%2$s;instanceName=%3$s",
				"com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
		DBServer odbc = new DBServer(
				"jdbc:odbc:%3$s",
				"sun.jdbc.odbc.JdbcOdbcDriver");
		
		servers.put("mysql",		my);
		servers.put("oracle",		ora);
		servers.put("sqlserver",	ms);
		servers.put("odbc", 		odbc);
	}
	
	public static DBServer get(String dbtype) {
		return servers.get(dbtype);
	}
	
	public static Iterator<String> getAllServer() {
		return servers.keySet().iterator();
	}
	
	/**
	 * 取得数据库类型列表的显示字符串
	 * @return - 用','号分隔的数据库类型
	 */
	public static String getTypeList() {
		StringBuilder buff = new StringBuilder();
		Iterator<String> it = getAllServer();
		while (it.hasNext()) {
			buff.append( it.next() );
			if (it.hasNext()) {
				buff.append(',');
			}
		}
		return buff.toString();
	}
}

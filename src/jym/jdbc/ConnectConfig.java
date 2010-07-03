// CatfoOD 2009-9-17 下午07:37:40

package jym.jdbc;

public class ConnectConfig {
	private String serverAdd;
	private String port;
	private String serverName;
	private String dbtype;
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/** 方法返回后删除密码 */
	public String getPassword() {
		String st = password;
		password = "";
		return st;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServerAdd() {
		return serverAdd;
	}
	public void setServerAdd(String serverAdd) {
		this.serverAdd = serverAdd;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	
	public boolean passwordIsNull() {
		return password==null || password.trim().length()==0;
	}
}

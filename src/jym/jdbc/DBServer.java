// CatfoOD 2009-9-16 下午03:32:55 
package jym.jdbc;

import jym.command.dbtype.IDBCommand;


public class DBServer {
	private String url;
	private String cn;
	private IDBCommand cmd;
	
	public DBServer(String urL, String classname) {
		url = urL;
		cn = classname;
		cmd = null;
	}
	
	/**
	 * 从连接配置中组装数据库jdbc url
	 * @param conf - 连接配置
	 * @return 装配好的url，不检查参数有效性
	 */
	public String getUrl(ConnectConfig conf) {
		return getUrl(
				conf.getServerAdd(),
				conf.getPort(),
				conf.getServerName()
				);
	}
	
	public String getUrl(String serverAdd, String port, String name) {
		return getUrl( new Object[]{
				serverAdd,
				port,
				name,
		});
	}
	
	public void registerDriver() throws ClassNotFoundException {
		Class.forName(cn);
	}
	
	/**
	 * 只有前三个参数有效
	 */
	private String getUrl(Object ... args) {
		return String.format(url, args);
	}
	
	public String getClassName() {
		return cn;
	}
	
	/**
	 * 取得与数据库绑定的命令集
	 */
	public IDBCommand getDBCommand() {
		return cmd;
	}
	
	/**
	 * 绑定一个命令集到数据库
	 * @return - 如果没有帮定任何命令集返回null
	 */
	public void setDBCommand(IDBCommand ic) {
		cmd = ic;
	}
}

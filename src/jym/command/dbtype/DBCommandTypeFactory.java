// CatfoOD 2009-9-19 上午09:05:22

package jym.command.dbtype;

import jym.jdbc.DBFactory;
import jym.jdbc.DBServer;

public final class DBCommandTypeFactory {
	
	static {
		init();
	}
	
	private static void init() {
		reg( new MySqlCommand(),	"mysql"		);
		reg( new SqlServerComm(),	"sqlserver"	);
		reg( new OracleCommand(),	"oracle"	);
	}
	
	private static void reg(IDBCommand idc, String dbname) {
		DBServer dbs = DBFactory.get(dbname);
		if (dbs!=null) {
			dbs.setDBCommand(idc);
		}
	}
	
	/**
	 * 返回数据库名字帮定的命令接口
	 * @param dbname - 数据库的类型名字
	 * @return - 不存在返回null
	 */
	public static IDBCommand getCommand(String dbname) {
		DBServer dbs = DBFactory.get(dbname);
		if (dbs!=null) {
			return dbs.getDBCommand();
		}
		return null;
	}
	
}

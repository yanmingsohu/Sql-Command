// CatfoOD 2009-9-22 下午03:31:40

package jym.command.list;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jym.command.ICommander;
import jym.command.IContainer;
import jym.command.IPrinter;
import jym.command.Login;
import jym.jdbc.SqlLinked;

public class CopyTableData implements ICommander {
	public static final String COMM = "cpda";

	@Override
	public boolean doit(IContainer d, String[] args) {
		Login login = d.getLogin();
		IPrinter ip = d.getPrinter();
		if (args.length==3) {
			try {
				new CopyData(ip, login, args);
				return true;
			} catch (Exception e) {
				ip.pl(e.getMessage());
			}
		}
		return false;
	}

	@Override
	public String getCommandName() {
		return COMM;
	}

	@Override
	public String getHelp() {
		return COMM + 
				" <目标连接>.<目标表名> <源连接>.<源表名>\n" +
				"\t 复制源表的所有数据行到目标表,只插入不修改\n" +
				"\t 注意表结构必须相同\n" +
				"\t *如果复制过程中出现约束冲突,复制的结果与特定DBMS的实现有关\n" +
				"\t *此时应检查复制结果";
	}

	@Override
	public String getInfo() {
		return "复制表内容";
	}

}

class CopyData {
	private SqlLinked srclink;
	private SqlLinked dsclink;
	private IPrinter ip;
	
	protected CopyData(IPrinter ip, Login login, String[] args) {
		Pack dsc = new Pack(args[1]);
		Pack src = new Pack(args[2]);
		srclink = login.getLink(src.linkname);
		dsclink = login.getLink(dsc.linkname);
		checkNull(srclink, "无效的连接名:"+src.linkname);
		checkNull(dsclink, "无效的连接名:"+dsc.linkname);
		this.ip = ip;
		start(dsc, src);
	}
	
	private void start(Pack dsc, Pack src) {
		String errStr = "";
		ResultSet srcset = null;
		PreparedStatement dscset = null;
		
		try {
			ip.pl("初始化...");
			errStr = "取得源表数据出错";
			srcset = srclink.exe("select * from " + src.tabname);
			errStr = "取得源表结构出错";
			String sql = getInsertSql(srcset, dsc.tabname);
			errStr = "构造目标表出错";
			dscset = dsclink.getConnection().prepareStatement(sql);
			
			ip.pl("开始复制...");
			copy(dscset, srcset);
			
		} catch (SQLException e) {
			ip.pl(errStr + " " + e.getMessage());
		} finally {
			SqlLinked.close(srcset);
			SqlLinked.close(dscset);
		}
	}
	
	private void copy(PreparedStatement dsc, ResultSet src) throws SQLException {
		try {
		src.setFetchSize(1000); } catch (Exception e) {}
		ResultSetMetaData rsm = src.getMetaData();
		int colc = rsm.getColumnCount();
		
		int row = 0;
		try {
			while (src.next()) {
				for (int i=1; i<=colc; ++i) {
					dsc.setObject(i, src.getObject(i));
				}
				dsc.addBatch();
				row++;
			}
			dsc.executeBatch();
			ip.pl("复制成功,共复制了"+row+"行数据");
		} catch (Exception e) {
			ip.pl("复制未完全成功:" + e.getMessage());
		}
	}
	
	private String getInsertSql(ResultSet r, String dscTableName) throws SQLException {
		String sql = "insert into %1$s (%2$s) values (%3$s)";
		StringBuilder names = new StringBuilder();
		StringBuilder valsig= new StringBuilder();
		getTableNames(r, names, valsig);
		
		return String.format( 
				sql, dscTableName, names.toString(), valsig.toString() );
	}
		
	private void getTableNames(ResultSet r, StringBuilder names, StringBuilder sig) 
	throws SQLException {
		ResultSetMetaData rsm = r.getMetaData();
		for (int i=1; i<=rsm.getColumnCount(); ++i) {
			names.append(rsm.getColumnName(i));
			sig.append('?');
			if (i<rsm.getColumnCount()) {
				names.append(',');
				sig.append(',');
			}
		}
	}
	
	public static final void checkNull(Object o, String text) {
		if (o==null)
			throw new NullPointerException(text);
	}
	
	private class Pack { 
		String linkname;
		String tabname;
		Pack(String s) {
			String[] p = s.split("\\.");
			linkname = p[0];
			tabname = p[1];
		}
	}
}


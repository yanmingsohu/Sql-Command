// CatfoOD 2009-9-22 下午08:10:27

package jym.command.list;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jym.command.ICommander;
import jym.command.IContainer;
import jym.command.IPrinter;
import jym.command.Login;
import jym.jdbc.SqlLinked;

public class CopyTableStrut implements ICommander {

	@Override
	public boolean doit(IContainer d, String[] args) {
		if (args.length==3) {
			new CopyStrut(d.getPrinter(), d.getLogin(), args);
			return true;
		}
		return false;
	}

	@Override
	public String getCommandName() {
		return "cpct";
	}

	@Override
	public String getHelp() {
		return "cpct - <目标连接>.<目标表名> <源连接>.<源表名>\n" +
				"\t 创建并复制源表表结构到目标表,并不复制约束等\n" +
				"\t 目标连接中不能有存在的表";
	}

	@Override
	public String getInfo() {
		return "创建并复制表结构";
	}

	
	private class CopyStrut {
		private Map<Integer, Sqltype> map;
		public static final char BLANK = ' ';
		
		private SqlLinked srclink;
		private SqlLinked dsclink;
		private IPrinter ip;
		
		private CopyStrut(IPrinter ip, Login login, String[] args) {
			Pack dsc = new Pack(args[1]);
			Pack src = new Pack(args[2]);
			srclink = login.getLink(src.linkname);
			dsclink = login.getLink(dsc.linkname);
			CopyData.checkNull(srclink, "无效的连接名:"+src.linkname);
			CopyData.checkNull(dsclink, "无效的连接名:"+dsc.linkname);
			this.ip = ip;
			start(dsc, src);			
		}
		
		private void start (Pack dscp, Pack srcp) {
			String erstr = "";
			try {
				ip.pl("初始化...");
				String sql1 = "select * from " + srcp.tabname;
				erstr = "取得源表数据出错";
				ResultSetMetaData src = srclink.exe(sql1).getMetaData();
				erstr = "取得目标数据结构出错";
				DatabaseMetaData dsc = dsclink.getConnection().getMetaData();
				erstr = "创建列类型出错";
				creatType(dsc);
				erstr = "初始化创建命令出错";
				String sql = creatSql(src, dsc, dscp.tabname);
				// ip.pl(sql);
				erstr = "创建目标表出错";
				dsclink.getStatement().execute(sql);
				ip.pl("[表结构复制完成]");
			} catch (SQLException e) {
				ip.pl(erstr +" "+ e.getMessage());
				// e.printStackTrace();
			}
		}
		
		private String creatSql(ResultSetMetaData src, DatabaseMetaData dsc, String dscname) 
		 throws SQLException 
		{
			Column[] col = new Column[src.getColumnCount()];
			for (int i=0; i<col.length; ++i) {
				col[i] = new Column(src, dsc, i+1);
			}
			StringBuilder buff = new StringBuilder();
			buff.append("CREATE TABLE ");
			buff.append(dscname);
			buff.append(" (");
			for (int i=0; i<col.length; ++i) {
				buff.append(col[i].getVelue());
				if (i<col.length-1)
					buff.append(',');
			}
			buff.append(" )");
			
			return buff.toString();
		}
		
		private class Column {
			private String colname;
			private String dsctype;
			private int size;
			private boolean notnull;
			private boolean autoincre;
			
			private Column(ResultSetMetaData src, DatabaseMetaData dsc, int col) 
			throws SQLException {
				Sqltype st = map.get(src.getColumnType(col));
				colname = src.getColumnName(col);
				size	= src.getPrecision(col);
				notnull = src.isNullable(col) == ResultSetMetaData.columnNoNulls;
				autoincre = src.isAutoIncrement(col);
				dsctype = st.typename;
				checkSize(st);
				formatColumnType();
			}
			
			private void checkSize(Sqltype st) {
				if (size>st.maxprecision) {
					ip.pl("<警告> 列'" + colname + "'的数据长度(" + size + 
							")超出了最大值(" + st.maxprecision+") ");
					size = st.maxprecision;
				}
			}
			
			private void formatColumnType() {
				if (size>0) {
					StringBuilder buff = new StringBuilder();
					String[] s = dsctype.split(" ");
					buff.append(s[0]);
					buff.append('(');
					buff.append(size);
					buff.append(')');
					for (int i=1; i<s.length; ++i) {
						buff.append(' ');
						buff.append(s[i]);
					}
					dsctype = buff.toString();
				}
			}
			
			protected String getVelue() {
				StringBuilder buff = new StringBuilder();
				buff.append(colname);
				 buff.append(BLANK);
				buff.append(dsctype);
				 buff.append(BLANK);
				if (notnull) {
					buff.append("NOT NULL");
				} else {
					buff.append("NULL");
				}
				 buff.append(BLANK);
				if (autoincre) {
					buff.append("AUTO_INCREMENT");
				}
				return buff.toString();
			}
		}
		
		private void creatType(DatabaseMetaData dsc) throws SQLException {
			ResultSet rs = dsc.getTypeInfo();
			map = new HashMap<Integer,Sqltype>();
			while (rs.next()) {
				Sqltype st = new Sqltype();
				st.typename = rs.getString("TYPE_NAME");
				st.datatype = rs.getInt("DATA_TYPE");
				st.maxprecision = (int)rs.getLong("PRECISION");
				if (map.containsKey(st.datatype)) {
					ip.pl("<警告>目标数据库含有重复的数据类型定义: "
							+ st.typename +"\t"+ st.datatype);
				} else {
					map.put(st.datatype, st);
				}
			}
		}
		
		private class Sqltype {
			String typename;
			int datatype;
			int maxprecision;
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
	
}

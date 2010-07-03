// CatfoOD 2009-9-22 下午03:38:04

package jym.command.list;

import java.sql.Connection;
import java.sql.SQLException;

import jym.command.ICommander;
import jym.command.IContainer;
import jym.command.IPrinter;
import jym.jdbc.SqlLinked;

public class AutoCommit implements ICommander {

	@Override
	public boolean doit(IContainer d, String[] args) {
		IPrinter ip = d.getPrinter();
		SqlLinked link = d.getCurrentLink();
		Connection conn = link.getConnection();
		
		try {
			if (args.length>1) {
				switchAuto(args[1], ip, conn);
			} else {
				disp(ip, conn);
			}
		} catch (Exception e) {
			ip.pl(e.getMessage());
		}
		return true;
	}
	
	protected void switchAuto(String sw, IPrinter ip, Connection conn) throws SQLException {
		boolean auto = sw.equalsIgnoreCase("on");
		conn.setAutoCommit(auto);
		if (auto) {
			ip.pl("[设置为自动递交模式]");
		} else {
			ip.pl("[设置为手动递交模式]");
		}
	}
	
	protected void disp(IPrinter ip, Connection conn) throws SQLException {
		if (conn.getAutoCommit()) {
			ip.pl("[当前为自动递交模式]");
		} else {
			ip.pl("[当前为手动递交模式]");
		}
	}

	@Override
	public String getCommandName() {
		return "acom";
	}

	@Override
	public String getHelp() {
		return "acom [off/on]\n" +
				"\t 打开/关闭自动递交,无参数显示当前状态";
	}

	@Override
	public String getInfo() {
		return "切换事务自动递交的状态";
	}

}

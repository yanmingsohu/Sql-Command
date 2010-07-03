// CatfoOD 2009-9-18 下午05:23:59

package jym.command.list;

import jym.command.ICommander;
import jym.command.IContainer;

public class DoSql implements ICommander {
	public static final String COMM = "sql";

	@Override
	public boolean doit(IContainer d, String[] args) {
		if (args.length>1) {
			StringBuilder buff = new StringBuilder();
			for (int i=1; i<args.length; ++i) {
				buff.append(args[i]);
				buff.append(' ');
			}
			d.doSql(buff.toString());
			return true;
		}
		return false;
	}

	@Override
	public String getCommandName() {
		return COMM;
	}

	@Override
	public String getHelp() {
		return "sql <sqlcommand> - 执行指定的sql语句";
	}

	@Override
	public String getInfo() {
		return "执行Sql语句";
	}

}

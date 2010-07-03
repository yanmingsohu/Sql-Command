// CatfoOD 2009-9-21 上午09:10:26

package jym.command.list;

import jym.command.ICommander;
import jym.command.IContainer;

public class DoSqlAllLink implements ICommander {

	@Override
	public boolean doit(IContainer d, String[] args) {
		if (args.length>1) {
			
			args[0] = DoSql.COMM;
			doAllLinkCommand(d, args);
			
			return true;
		}
		return false;
	}
	
	protected static void doAllLinkCommand(IContainer d, String[] args) {
		final int c = d.getLogin().getLinkCount();
		for (int i=0; i<c; ++i) {
			d.doCommand(SwitchLink.COMM);
			d.doCommand(args);
		}
	}

	@Override
	public String getCommandName() {
		return "ds";
	}

	@Override
	public String getHelp() {
		return "ds <sql> - 在所有可用的连接中执行sql语句";
	}

	@Override
	public String getInfo() {
		return "在所有可用的连接中依次执行sql语句";
	}

}

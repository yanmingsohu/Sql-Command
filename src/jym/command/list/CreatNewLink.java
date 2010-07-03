// CatfoOD 2009-9-18 下午04:45:21

package jym.command.list;

import jym.command.ICommander;
import jym.command.IContainer;
import jym.command.IPrinter;
import jym.jdbc.SqlLinked;

public class CreatNewLink implements ICommander {

	@Override
	public boolean doit(IContainer d, String[] args) {
		IPrinter ip = d.getPrinter();
		SqlLinked link = d.getLogin().loginFormConsole();
		if (link!=null) {
			d.doCommand("sw " + link.getName());
		} else {
			ip.pl("连接建立失败");
		}
		return true;
	}

	@Override
	public String getCommandName() {
		return "cnl";
	}

	@Override
	public String getHelp() {
		return "建立新的连接";
	}

	@Override
	public String getInfo() {
		return getHelp();
	}
	
}

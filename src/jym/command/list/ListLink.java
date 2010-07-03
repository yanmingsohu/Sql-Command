// CatfoOD 2009-9-18 下午03:58:24

package jym.command.list;

import java.util.Iterator;

import jym.command.ICommander;
import jym.command.IContainer;
import jym.command.IPrinter;
import jym.command.Login;
import jym.helper.FormatOut;

public class ListLink implements ICommander {

	@Override
	public boolean doit(IContainer d, String[] args) {
		IPrinter ip = d.getPrinter();
		Login login = d.getLogin();
		Iterator<String> it = login.getLinkedNames();
		FormatOut out = new FormatOut();
		
		ip.pl("\n[可用的数据库连接:]");
		while (it.hasNext()) {
			login.getSqlLinkConfig( login.getLink(it.next()), out );
		}
		ip.pl(out);
		
		return true;
	}

	@Override
	public String getHelp() {
		return "列出可用的数据库连接";
	}

	@Override
	public String getCommandName() {
		return "lisk";
	}

	@Override
	public String getInfo() {
		return getHelp();
	}

}

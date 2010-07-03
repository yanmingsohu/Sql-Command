// CatfoOD 2009-9-18 ÉÏÎç11:51:52 
package jym.command.list;

import jym.command.ICommander;
import jym.command.IContainer;

public class Help implements ICommander {
	private final String command = "help";

	@Override
	public boolean doit(IContainer d, String[] args) {
		if (args.length>1){
			d.help(args[1]);
		}
		else {
			d.listCommand();
		}
		return true;
	}

	@Override
	public String getHelp() {
		return "°ïÖú";
	}

	@Override
	public String getCommandName() {
		return command;
	}

	@Override
	public String getInfo() {
		return getHelp();
	}

}

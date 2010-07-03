// CatfoOD 2009-9-18 上午11:19:08 

package jym.command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.helper.ConfigFile;
import jym.helper.Displayable;
import jym.helper.FormatOut;
import jym.helper.Saver;
import jym.helper.TablePrinter;
import jym.jdbc.SqlLinked;

public class CommandDo implements IContainer {
	
	private Displayable disp;
	private ConfigFile config;
	private TablePrinter table;
	private Saver log;
	private Map<String,ICommander> com;
	private boolean exited = false;
	private Login login;
	private SqlLinked link;
	
	public CommandDo() {
		disp 	= new Displayable();
		disp.printWelcome();
		config	= new ConfigFile("connect.ini");
		table	= new TablePrinter(disp);
		log		= new Saver("sql.log");
		com		= new HashMap<String,ICommander>();
		login	= new Login(disp);
		login.loginFromFileConfig(config);
		initCommander();
	}
	
	private void initCommander() {
		CommandCreater.initCommander(com);
	}
	
	public void start() {
		link = login.getLink();
		while (link==null) {
			link = login.loginFormConsole();
		}
		
		while (!exited) {
			String sql = disp.inSql( link.getName() );
			
			if (doCommand(sql)) {
				pl("doCommand OK.");
			} 
			else if (doSql(sql)) {
				pl("Query OK.");
			}
			else {
				continue;
			}
			save(sql);
		}
	}
	
	public boolean doCommand(String commands) {
		String[] args = commands.split(" ");
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<args.length; ++i) {
			if (args[i]!=null && args[i].trim().length()>1) {
				list.add(args[i]);
			}
		}
		return doCommand(list.toArray(new String[0]));
	}
	
	public boolean doCommand(String[] args) {		
		if (args.length>0) {
			ICommander ic = com.get(args[0]);
			if (ic!=null) {
				try {
					if (ic.doit(this, args)) {
						// ..
					} else {
						pl("[命令帮助]");
						pl(ic.getHelp());
					}
					return true;
				} catch (Exception e) {
					pl( e.getMessage() );
				}
			}
		}
		return false;
	}
	
	public boolean doSql(String sql) {
		try {
			Statement state = link.getStatement();
			
			if (state.execute(sql)) {
				int arow = state.getUpdateCount();
				
				if (arow>0) {
					disp.pl(arow + " rows affected.");
				} else {
					ResultSet rs = state.getResultSet();
					table.printTable(rs);
					rs.close();
				}
			}
			return true;
			
		} catch (Exception e) {
			disp.pl(e.getMessage());
			// e.printStackTrace();
		}
		
		return false;
	}
	
	public void save(String text) {
		log.put(text);
		log.flush();
	}
	
	public void exit() {
		exited = true;
		log.close();
		login.closeAll();
	}
	
	public void pl(Object o) {
		disp.pl(o);
	}
	
	public void listCommand() {
		FormatOut out = new FormatOut();
		out.pl("[命令列表:]");
//		out.p(FormatOut.getSymbol( 8, '-'));
//		out.pl(FormatOut.getSymbol(30, '-'));
		
		Iterator<String> it = com.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			out.p(key);
			out.p(com.get(key).getInfo());
			out.pl();
		}
		disp.p( out.toString() );
	}
	
	public void help(String command) {
		ICommander ic = com.get(command);
		if (ic!=null) {
			pl(command + ":");
			pl(ic.getHelp());
		} else {
			pl("无效的命令");
		}
	}

	@Override
	public boolean switchLink(String linkname) {
		SqlLinked sl = login.getLink(linkname);
		if (sl!=null) {
			link = sl;
			return true;
		}
		return false;
	}

	@Override
	public Login getLogin() {
		return login;
	}

	@Override
	public SqlLinked getCurrentLink() {
		return link;
	}

	@Override
	public IPrinter getPrinter() {
		return disp;
	}
}

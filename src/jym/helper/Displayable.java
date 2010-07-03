// CatfoOD 2009-9-17 下午07:44:56

package jym.helper;

import java.io.Console;

import jym.command.IPrinter;


public final class Displayable implements IPrinter {
	private PrintOutBuffer out = new PrintOutBuffer();
	
	public void printWelcome() {
		String wel = "WelcoMe JaVa jdbC Command. " + Version.VERSION;
		int len = (79-wel.length()) / 2;
		String s = FormatOut.getSymbol(len, '-');
		wel = s + wel + s;
		
		pl(wel);
		pl("CatfoOD 2009 java ProjEct");
		pl("[命令以';'结尾,'q'退出程序,'help'打印帮助.]");
		pl();
	}
	
	// ------------ 输入的方法
	public String in() {
		return in("sql");
	}
	
	public String in(String sig) {
		return in(sig, false);
	}
	
	public String inpw(String sig) {
		return in(sig, false, true);
	}
	
	public String in(String sig, boolean empty) {
		return in(sig, empty, false);
	}
	
	private String in(String sig, boolean empty, boolean hide) {
		StringBuffer buff = new StringBuffer();
		Console inpw = System.console();
		try {
			do {
				buff.delete(0, buff.length());
				
				p(sig+":> ");
				
				if (inpw==null || !hide) {
					int ch = System.in.read();
					while (ch!='\n') {
						buff.append( (char)ch );
						ch = System.in.read();
					}
				} else {
					char[] pw = inpw.readPassword();
					buff.append(pw);
				}
				sig = FormatOut.getBlank(sig.length());
			} while (!empty && buff.toString().trim().length()<1);
			
		} catch(Exception e) {
			pl("system error.\nbye.");
			System.exit(1);
		}
		return buff.toString().trim();
	}
	
	public String inSql(String sig) {
		String bl = FormatOut.getBlank(sig.length());
		
		String sql;
		StringBuilder buff = new StringBuilder();
		
		while (true) {
			sql = in(sig);
			if (sql.endsWith(";")) {
				buff.append( sql.substring(0, sql.length()-1) );
				break;
			} else {
				buff.append( sql );
			}
			sig = bl;
		}
		
		return buff.toString();
	}
	
	// ------------ 输出的方法
	public void p(Object o) {
		out.p(o);
	}
	public void pl(Object o) {
		p(o+"\n");
	}
	public void pl() {
		p("\n");
	}
}

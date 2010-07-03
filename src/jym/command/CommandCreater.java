// CatfoOD 2009-9-18 …œŒÁ11:58:33 
package jym.command;

import java.io.File;
import java.util.Map;

import jym.command.list.*;
import jym.helper.CommonFileFilter;

public class CommandCreater {
	
	public final static String PATH = "bin/jym/command/list";
	public final static String PACKAGE = "jym.command.list.";
	
	public static void initCommander(Map<String,ICommander> m) {
		creatCommands(PATH, PACKAGE, m);
	}
	
	private static void creatCommands(String path, String pack, Map<String,ICommander> m) {
		File[] fs = getFileFromDir(path);
		for (int i=0; i<fs.length; ++i) {
			ICommander ic = creatCommand(fs[i], pack);
			registerCommand(ic, m);
		}
	}
	
	private static void registerCommand(ICommander ic, Map<String, ICommander> map) {
		if (ic!=null) {
			map.put(ic.getCommandName(), ic);
		}
	}
	
	private static File[] getFileFromDir(String path) {
		File f = new File(path);
		CommonFileFilter cff = new CommonFileFilter();
		cff.add("class");
		
		return f.listFiles(cff);
	}
	
	private static ICommander creatCommand(File file, String pack) {
		try {
			String name = file.getName();
			int end = name.lastIndexOf('.');
			name = name.substring(0, end);
			
			Class<?> cl = Class.forName(pack + name);
			Object obj = cl.newInstance();
			if (obj instanceof ICommander) {
				return (ICommander) obj;
			}
			
		} catch (InstantiationException ie) {
			// do noting.
		} catch (Exception e) {
			System.out.println("[√¸¡Ó‘ÿ»Î¥ÌŒÛ:" + e.getMessage() + "]");
		}
		return null;
	}
	
}

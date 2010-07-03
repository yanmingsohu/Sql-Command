// CatfoOD 2009-9-18 上午11:33:53 
package jym.command;

/**
 * 每个命令必须实现的方法，同时提供一个无参构造
 */
public interface ICommander {
	/**
	 * 命令被调用<b>命令可以抛出Exception的子类</b>
	 * @param d - 可以执行的操作
	 * @param args - 传入的参数 args[0] 为命令的名字
	 * @return - 成功返回true,失败返回false,此时容器负责打印帮助
	 */
	boolean doit(IContainer d, String[] args);
	
	/**
	 * 取得命令的名字
	 */
	String getCommandName();
	
	/**
	 * 取得帮助字符串
	 */
	String getHelp();
	
	/**
	 * 取得简短的说明
	 */
	String getInfo();
}

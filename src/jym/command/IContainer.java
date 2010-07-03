// CatfoOD 2009-9-18 下午03:25:48

package jym.command;

import jym.jdbc.SqlLinked;

public interface IContainer {
	/**
	 * 打印指定命令的详细帮助
	 * @param command
	 */
	void help(String command);
	/**
	 * 退出系统
	 */
	void exit();
	/**
	 * 列出所有可用命令
	 */
	void listCommand();
	/**
	 * 执行sql语句
	 * @return 成功返回true
	 */
	boolean doSql(String sql);
	/**
	 * 执行一个命令 
	 * @return 成功返回true
	 */
	boolean doCommand(String commands);
	/**
	 * 执行一个命令 
	 * @param args - 参数0为命令名
	 * @return 成功返回true
	 */
	boolean doCommand(String[] args);
	/**
	 * 切换到指定的连接名
	 * @param linkname - 指定的连接名linkname必须是Login.getLinkedNames()中的元素
	 * @return
	 */
	boolean switchLink(String linkname);
	/**
	 * 取得当前登录的信息
	 * @return 返回的Loing对象中，保存有可用连接组
	 */
	Login getLogin();
	/**
	 * 取得正在使用的连接
	 * @return 返回的SqlLinked对象可以执行sql语句，并可取得连接信息
	 */
	SqlLinked getCurrentLink();
	/**
	 * 取得的打印对象，提供输出到用户界面的功能<br>
	 * <b>不要直接使用System.out.println()等输出</b>
	 */
	IPrinter getPrinter();
}

// CatfoOD 2009-9-3 上午07:45:46

package jym.helper;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;


/**
 * 通用文件扩展名过滤器
 * v0.2
 */
public class CommonFileFilter implements FileFilter {
	/**
	 * 包含模式，只有指定的扩展名允许通过
	 */
	public static final FilterModel INCLUDE_MODEL = new FilterModel("include", false);
	/**
	 * 排斥模式，指定的扩展名禁止通过
	 */
	public static final FilterModel EXCLUDE_MODEL = new FilterModel("exclude", true);
	
	private ArrayList<String> list;
	private FilterModel model;
	private boolean dir = false;
	
	
	/**
	 * 创建一个'包含模式'文件过滤器，默认文件夹无法通过<br>
	 * 在add()扩展名之前，任何文件都无法通过
	 */
	public CommonFileFilter() {
		list = new ArrayList<String>();
		model = INCLUDE_MODEL;
	}
	
	/**
	 * 设置文件过滤器的过滤模式，见常量字段<br>
	 * 注意：重新设置模式后，记住可能需要清空扩展名列表
	 * @param model - 过滤模式
	 */
	public void setModel(FilterModel model) {
		if (model!=null) {
			this.model = model;
		}
	}
	
	/**
	 * 设置文件夹是否允许通过，默认禁止通过
	 * 文件夹名，并不不做扩展名检查
	 */
	public void dirCanAccept(boolean dir) {
		this.dir = dir;
	}
	
	/**
	 * 把指定的扩展名加入模型列表
	 * @param exname - 以'.'开始的文件扩展名，如果没有以'.'开始，自动添加'.'
	 */
	public void add(String exname) {
		exname = check(exname);
		list.add(exname);
	}
	
	/**
	 * 移除指定的扩展名，如果扩展名不存在列表中，则什么都不做
	 * @param exname - 以'.'开始的文件扩展名，如果没有以'.'开始，自动添加'.'
	 */
	public void remove(String exname) {
		exname = check(exname);
		list.remove(exname);
	}
	
	/**
	 * 清空全部指定的扩展名
	 */
	public void removeAll() {
		list.clear();
	}
	
	/**
	 * 自动添加'.'
	 */
	private String check(String name) {
		if (name!=null && name.length()>0) {
			if (!name.startsWith(".")) {
				name = "." + name;
			}
		}
		return name;
	}

	@Override
	public boolean accept(File file) {
		if (!dir && file.isDirectory()) return false;
		else if (file.isDirectory()) return true;
		
		boolean accept = model.getMode();
		
		String name = file.getName();
		for (int i=0; i<list.size(); ++i) {
			if ( name.endsWith(list.get(i)) ) {
				accept = !accept;
				break;
			}
		}
		return accept;
	}
	
	/**
	 * 返回FilenameFilter的便捷方法，行为与FileFilter相同<br>
	 * 注意对CommonFileFilter的修改直接影响返回的FilenameFilter<br>
	 * 返回的FilenameFilter做相等性比较总是返回false
	 */
	public FilenameFilter getFilenameFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				dir = new File(dir, name);
				return CommonFileFilter.this.accept(dir);
			}
		};
	}

	/**
	 * 过滤器模型安全枚举
	 */
	private static class FilterModel {
		private String _name;
		private boolean _mode;
		private FilterModel(String name, boolean mode) {
			_name = name;
			_mode = mode;
		};
		public String toString() { return _name; }
		public boolean getMode() { return _mode; }
	}
}

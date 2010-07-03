// CatfoOD 2009-9-18 下午02:50:37

package jym.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.helper.ConfigFile;
import jym.helper.Displayable;
import jym.helper.FormatOut;
import jym.jdbc.ConnectConfig;
import jym.jdbc.DBFactory;
import jym.jdbc.SqlLinked;

public class Login {
	private Displayable d;
	private Map<String,SqlLinked> links;
	
	public Login(Displayable d) {
		this.d = d;
		links = new HashMap<String,SqlLinked>();
	}
	
	// ------------ 登录的方法	
	private ConnectConfig inputConfig() {
		ConnectConfig conf = new ConnectConfig();
		conf.setDbtype(		dbtype()						);
		conf.setServerAdd(	d.in("Server Address")			);
		conf.setPort(		d.in("Server Port")				);
		conf.setServerName(	d.in("DB Name(instance name)")	);
		conf.setUsername(	d.in("User Name")				);
		conf.setPassword(	d.inpw("Password")				);
		return conf;
	}
	
	private String dbtype() {
		d.pl("有效的 DB Type: " + DBFactory.getTypeList());
		String dbt = null;
		while (true) {
			dbt = d.in("DB Type");
			if (DBFactory.get(dbt)!=null) {
				break;
			}
		}
		return dbt;
	}
	
	private String linkname() {
		String name = null;
		while (true) {
			name = d.in("link name");
			if (links.containsKey(name)) {
				d.pl("['"+ name +"' 这个名字已经使用]");
			} else {
				break;
			}
		}
		return name;
	}
	
	public SqlLinked loginFormConsole() {
		d.pl("--------- [ 配置文件中没有可用的连接,请输入登录信息 ] ---------");
		String name = linkname();
		ConnectConfig conf = inputConfig();
		SqlLinked link = null;
		try {
			link = new SqlLinked(conf, name);
			saveLink(link);
			d.pl();
			d.pl( getSqlLinkConfig(link) );
		} catch (Exception e) {
			d.pl(e);
		}
		return link;
	}
	
	public Iterator<String> getLinkedNames() {
		return links.keySet().iterator();
	}
	
	public SqlLinked getLink(String name) {
		return links.get(name);
	}
	
	/**
	 * 取得可用的连接
	 */
	public SqlLinked getLink() {
		 Iterator<String> it = getLinkedNames();
		 if (it.hasNext()) {
			 return getLink(it.next());
		 }
		 return null;
	}
	
	public int getLinkCount() {
		return links.size();
	}
	
	public void loginFromFileConfig(ConfigFile file) {
		Map<String, ConnectConfig> maps = file.getConnections();
		Iterator<String> it = maps.keySet().iterator();
		StringBuilder buff = new StringBuilder();
		FormatOut mess = new FormatOut();
		
		while (it.hasNext()) {
			String name = it.next();
			ConnectConfig conf = maps.get(name);
			
			if (conf.passwordIsNull()) {
				d.pl("[为" + name + "配置输入密码]");
				conf.setPassword( d.inpw(name) );
			}

			try {
				SqlLinked link = new SqlLinked(conf, name);
				saveLink(link);
				getSqlLinkConfig(link, mess);
			} catch (Exception e) {
				buff.append("[连接到'");
				buff.append(name);
				buff.append("'失败]\n-- ");
				buff.append(e.getMessage());
				buff.append('\n');
			}
		}
		d.pl();
		d.pl( buff.toString() );
		
		if (links.size()>0) {
			d.pl("[可使用以下连接:]");
			d.pl( mess.toString() );
		}
	}
	
	protected void closeAll() {
		Iterator<String> it = getLinkedNames();
		while (it.hasNext()) {
			getLink(it.next()).close();
		}
		links.clear();
	}
	
	private void saveLink(SqlLinked link) {
		links.put(link.getName(), link);
	}
	
	public String getSqlLinkConfig(SqlLinked link) {
		FormatOut out = new FormatOut();
		getSqlLinkConfig(link, out);
		return out.toString();
	}
	
	public void getSqlLinkConfig(SqlLinked link, FormatOut out) {
		ConnectConfig conf = link.getConfig();
		String name = link.getName();
		out.p("-- Link Name: " + name);
		out.p("   DB Server: " + conf.getDbtype());
		out.p("   User name: " + conf.getUsername());
		out.pl();
	}
}

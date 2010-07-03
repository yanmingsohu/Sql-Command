// CatfoOD 2009-9-17 下午07:35:07

package jym.helper;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jym.jdbc.ConnectConfig;

public class ConfigFile {
	private Map<String, ConnectConfig> connamesMap;
	private Properties p;
	
	public ConfigFile(String file) {
		try {
			connamesMap = new HashMap<String, ConnectConfig>();
			p = new Properties();
			p.load( new FileInputStream(file) );
			init();
		} catch (Exception e) {
			System.out.println("配置文件加载失败. " + e.getMessage());
		}
	}
	
	public Map<String, ConnectConfig> getConnections() {
		return connamesMap;
	}
	
	private void init() {
		String[] connames = getConnecting();
		for (int i=0; i<connames.length; ++i) {
			ConnectConfig value = getConn( connames[i] );
			connamesMap.put(connames[i], value);
		}
	}
	
	private ConnectConfig getConn(String conname) {		
		ConnectConfig c = new ConnectConfig();
		c.setDbtype(		p.getProperty(conname + "_type")		);
		c.setPort( 			p.getProperty(conname + "_port")		);
		c.setServerAdd( 	p.getProperty(conname + "_address")		);
		c.setServerName( 	p.getProperty(conname + "_servername")	);
		c.setUsername(		p.getProperty(conname + "_name")		);
		c.setPassword(		p.getProperty(conname + "_pass")		);
		return c;
	}
	
	private String[] getConnecting() {
		String[] conns = new String[0];
		String cs = p.getProperty("connects");
		if (cs!=null) {
			conns = cs.split(",");
		}
		return conns;
	}
}

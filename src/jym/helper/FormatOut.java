package jym.helper;
import java.util.ArrayList;
import java.util.List;

// CatfoOD 2009-9-17 ÏÂÎç04:24:47 

public class FormatOut {
	private int ml = 6;
	private List<String>list;
	
	public FormatOut() {
		list = new ArrayList<String>();
	}
	
	public void pl() {
		p("\n");
	}
	
	public void pl(Object s) {
		p(s);
		pl();
	}
	
	public void p(Object o) {
		if (o==null) {
			p("null");
		} else {
			p(o.toString());
		}
	}
	
	public void p(String s) {
		list.add(s);
		if (s.length()>ml) {
			ml = s.length();
		}
	}
	
	public int getLength() {
		return ml;
	}
	
	public static String getBlank(int len) {
		return getSymbol(len, ' ');
	}
	
	public static String getSymbol(int len, char s) {
		StringBuilder buff = new StringBuilder();
		for (int i=0; i<len; ++i) {
			buff.append(s);
		}
		return buff.toString();
	}
	
	public static String getMaxLen(String text, int maxlen) {
		if (text.length()>maxlen) {
			return text.substring(0, maxlen);
		}
		else {
			return text;
		}
	}
	
	public String toString() {
		StringBuilder buff = new StringBuilder();
		ml+=2;
		for (int i=0; i<list.size(); ++i) {
			String s = list.get(i);
			buff.append(s);
			if (!s.equals("\n")) {
				int bl = ml - s.length();
				for (int j=0; j<bl; ++j) {
					buff.append(' ');
				}
			}
		}
		list.clear();
		ml = 6;
		return buff.toString();
	}
}
